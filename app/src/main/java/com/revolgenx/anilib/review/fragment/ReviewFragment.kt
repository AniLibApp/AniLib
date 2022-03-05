package com.revolgenx.anilib.review.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.databinding.ReviewFragmentLayoutBinding
import com.revolgenx.anilib.common.event.OpenMediaInfoEvent
import com.revolgenx.anilib.common.event.OpenReviewComposerEvent
import com.revolgenx.anilib.common.event.OpenUserProfileEvent
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.social.factory.AlMarkwonFactory
import com.revolgenx.anilib.social.markwon.AlStringUtil.anilify
import com.revolgenx.anilib.type.ReviewRating
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.review.viewmodel.ReviewVM
import com.revolgenx.anilib.util.openLink
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewFragment : BaseToolbarFragment<ReviewFragmentLayoutBinding>() {
    override var titleRes: Int? = R.string.review
    override val menuRes: Int = R.menu.review_fragment_menu

    companion object {
        private const val REVIEW_ID_KEY = "REVIEW_ID_KEY"
        fun newInstance(reviewId: Int) = ReviewFragment().also {
            it.arguments = bundleOf(REVIEW_ID_KEY to reviewId)
        }
    }

    private val viewModel by viewModel<ReviewVM>()
    private val reviewModel get() = viewModel.review
    private val rateReviewField get() = viewModel.rateReviewField

    private val reviewId: Int? get() = arguments?.getInt(REVIEW_ID_KEY)

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ReviewFragmentLayoutBinding {
        return ReviewFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.field.reviewId = reviewId ?: return

        viewModel.reviewLiveData.observe(viewLifecycleOwner) { res ->
            when (res) {
                is Resource.Success -> {
                    showLoading(false)
                    binding.bind()
                }
                is Resource.Error -> {
                    showError()
                }
                is Resource.Loading -> {
                    showLoading(true)
                }
            }
        }

        viewModel.rateReviewLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.bindRating()
                }
                is Resource.Error -> {
                    makeToast(R.string.operation_failed)
                    binding.bindRating()
                }
                is Resource.Loading -> {}
            }
        }

        if (savedInstanceState == null) {
            viewModel.getReview()
        }
    }

    private fun ReviewFragmentLayoutBinding.bind() {
        reviewModel?.apply {
            reviewMediaBannerImage.setImageURI(
                media?.bannerImage ?: media?.coverImage?.largeImage
            )
            reviewMediaTitleTv.text = media!!.title!!.title(requireContext())
            reviewMediaTitleTv.setOnClickListener {
                media?.let { item ->
                    OpenMediaInfoEvent(
                        MediaInfoMeta(
                            item.id,
                            item.type!!,
                            item.title!!.romaji!!,
                            item.coverImage!!.image(requireContext()),
                            item.coverImage!!.largeImage,
                            item.bannerImage
                        )
                    ).postEvent
                }
            }
            reviewByIv.setImageURI(user?.avatar?.image)
            reviewByTv.text = getString(R.string.review_by).format(user?.name)
            createdAtTv.text = createdAtDate
            reviewByScoreTv.text =
                getString(R.string.review_score_format).format(score?.toString())

            AlMarkwonFactory.getMarkwon().setMarkdown(reviewTv, anilify(body ?: ""))

            binding.reviewByIv.setOnClickListener {
                OpenUserProfileEvent(user?.id).postEvent
            }

            bindRating()
            bindReviewToolbar()
        }
    }

    private fun ReviewFragmentLayoutBinding.bindRating() {
        updateReviewRatingButton()
        bindRatingAmount()
        reviewLikeIv.setOnClickListener {
            if (requireContext().loggedIn()) {
                viewModel.upVoteReview()
            } else {
                makeToast(R.string.please_log_in, null, R.drawable.ic_person)
            }
        }

        reviewDisLikeIv.setOnClickListener {
            if (requireContext().loggedIn()) {
                viewModel.downVoteReview()
            } else {
                makeToast(R.string.please_log_in, null, R.drawable.ic_person)
            }
        }
    }

    private fun ReviewFragmentLayoutBinding.updateReviewRatingButton() {
        resetLikeDislike()
        when (rateReviewField.userRating) {
            ReviewRating.UP_VOTE.ordinal -> {
                reviewLikeIv.colorType = Theme.ColorType.ACCENT
            }
            ReviewRating.DOWN_VOTE.ordinal -> {
                reviewDisLikeIv.colorType = Theme.ColorType.ACCENT
            }
            else -> {

            }
        }
    }


    private fun ReviewFragmentLayoutBinding.bindRatingAmount() {
        val review = reviewModel ?: return
        reviewLikesInfo.text =
            getString(R.string.s_out_of_s_liked_this_review).format(
                review.rating,
                review.ratingAmount
            )
    }


    private fun resetLikeDislike() {
        binding.reviewLikeIv.colorType = Theme.ColorType.TINT_SURFACE
        binding.reviewDisLikeIv.colorType = Theme.ColorType.TINT_SURFACE
    }

    override fun updateToolbar() {
        super.updateToolbar()
        bindReviewToolbar()
    }

    private fun bindReviewToolbar() {
        getBaseToolbar().menu.let {
            it.findItem(R.id.review_edit_menu)?.isVisible = viewModel.showEdit
            it.findItem(R.id.review_open_link)?.isVisible =
                reviewModel?.siteUrl.isNullOrBlank().not()
        }
    }

    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.review_edit_menu -> {
                reviewId?.let {
                    reviewModel?.mediaId?.let {
                        OpenReviewComposerEvent(it).postEvent
                    }
                }
                true
            }
            R.id.review_open_link -> {
                reviewModel?.siteUrl?.let {
                    requireContext().openLink(it)
                }
                true
            }
            else -> super.onToolbarMenuSelected(item)
        }
    }

    private fun showLoading(b: Boolean) {
        binding.resourceStatusContainer.resourceStatusContainer.visibility =
            if (b) View.VISIBLE else View.GONE
        binding.resourceStatusContainer.resourceProgressLayout.progressLayout.visibility =
            if (b) View.VISIBLE else View.GONE
        binding.resourceStatusContainer.resourceErrorLayout.errorLayout.visibility = View.GONE
    }

    private fun showError() {
        binding.resourceStatusContainer.resourceStatusContainer.visibility = View.VISIBLE
        binding.resourceStatusContainer.resourceErrorLayout.errorLayout.visibility = View.VISIBLE
        binding.resourceStatusContainer.resourceProgressLayout.progressLayout.visibility = View.GONE
    }
}