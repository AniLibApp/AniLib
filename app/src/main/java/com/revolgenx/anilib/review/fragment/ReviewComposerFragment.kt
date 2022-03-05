package com.revolgenx.anilib.review.fragment

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.UserPreference
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.databinding.ReviewComposerFragmentLayoutBinding
import com.revolgenx.anilib.common.event.OpenReviewEvent
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.review.viewmodel.ReviewComposerVM
import com.revolgenx.anilib.ui.view.makeConfirmationDialog
import com.revolgenx.anilib.ui.view.makeErrorToast
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.getClipBoardText
import com.revolgenx.anilib.util.openLink
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewComposerFragment : BaseToolbarFragment<ReviewComposerFragmentLayoutBinding>() {
    companion object {
        private const val REVIEW_MEDIA_ID_KEY = "REVIEW_MEDIA_ID_KEY"

        fun newInstance(mediaId: Int) = ReviewComposerFragment().also {
            it.arguments = bundleOf(REVIEW_MEDIA_ID_KEY to mediaId)
        }
    }

    override var titleRes: Int? = R.string.review_composer

    private val viewModel by viewModel<ReviewComposerVM>()
    private val saveField get() = viewModel.saveField


    private val reviewMediaId get() = arguments?.getInt(REVIEW_MEDIA_ID_KEY)

    override var setHomeAsUp: Boolean = true
    override val menuRes: Int = R.menu.review_composer_menu

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ReviewComposerFragmentLayoutBinding {
        return ReviewComposerFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun updateToolbar() {
        super.updateToolbar()
        updateComposerToolbar()
    }

    private fun updateComposerToolbar() {
        getBaseToolbar().menu.let {
            it.findItem(R.id.deleteReview)?.isVisible = viewModel.showDelete
            it.findItem(R.id.open_review_menu)?.isVisible = reviewMediaId != null
        }
    }

    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.deleteReview -> {
                makeConfirmationDialog(
                    requireContext()
                ) {
                    viewModel.deleteReview()
                }
                true
            }
            R.id.saveReview -> {
                binding.checkReview {
                    viewModel.saveReview()
                    makeToast(R.string.saved_successfully)
                }
                true
            }
            R.id.open_review_menu -> {
                reviewMediaId?.let {
                    requireContext().openLink(getString(R.string.review_editor_url).format(it))
                }
                true
            }
            else -> return super.onToolbarMenuSelected(item)
        }
    }

    private fun ReviewComposerFragmentLayoutBinding.checkReview(pass: () -> Unit) {
        if (saveField.body?.length ?: 0 < 2200) {
            reviewErrorTv.visibility = View.VISIBLE
            return
        }

        if (saveField.summary?.length ?: 0 < 20) {
            reviewSummaryErrorTv.visibility = View.VISIBLE
            return
        }
        pass.invoke()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.field.mediaId = reviewMediaId ?: return
        viewModel.field.userId = UserPreference.userId
        viewModel.saveField.mediaId = reviewMediaId

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

        viewModel.saveReviewLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    showLoading(false)
                    it.data?.let {
                        OpenReviewEvent(it).postEvent
                    }
                    popBackStack()
                }
                is Resource.Error -> {
                    showLoading(false)
                    makeErrorToast(R.string.failed_to_save)
                }
                is Resource.Loading -> showLoading(true)
            }
        }

        viewModel.deleteLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> popBackStack()
                is Resource.Error -> {
                    showLoading(false)
                    makeErrorToast(R.string.failed_to_delete)
                }
                is Resource.Loading -> showLoading(true)
            }
        }

        if (savedInstanceState == null) {
            viewModel.getReview()
        }
    }

    private fun ReviewComposerFragmentLayoutBinding.bind() {
        reviewContainerLayout.visibility = View.VISIBLE
        saveField.body?.let {
            reviewEt.setText(it)
        }

        saveField.summary?.let {
            reviewSummaryEt.setText(it)
        }

        reviewPrivateCheckBox.isChecked = viewModel.saveField.private

        reviewScoreCountTv.updateCount(saveField.score)

        reviewCount.text = getString(R.string.d_d).format(saveField.body?.length ?: 0, 2200)
        reviewEt.doOnTextChanged { text, _, _, _ ->
            reviewErrorTv.visibility = View.GONE
            reviewCount.text = getString(R.string.d_d).format(text?.length ?: 0, 2200)
            saveField.body = text?.toString()
        }

        reviewSummaryCount.text =
            getString(R.string.d_d).format(saveField.summary?.length ?: 0, 120)
        reviewSummaryEt.doOnTextChanged { text, _, _, _ ->
            reviewSummaryErrorTv.visibility = View.GONE
            reviewSummaryCount.text = getString(R.string.d_d).format(text?.length ?: 0, 120)
            saveField.summary = text?.toString()
        }

        reviewPrivateCheckBox.setOnCheckedChangeListener { _, isChecked ->
            saveField.private = isChecked
        }
        updateComposerToolbar()
        bindListener()
    }

    private fun ReviewComposerFragmentLayoutBinding.bindListener() {
        reviewSummaryPasteIv.setOnClickListener {
            reviewSummaryEt.append(requireContext().getClipBoardText())
        }

        binding.markdownHelperLayout.setTextEditView(reviewEt)
    }

    private fun showLoading(b: Boolean) {
        binding.resourceStatusLayout.resourceStatusContainer.visibility =
            if (b) View.VISIBLE else View.GONE
        binding.resourceStatusLayout.resourceProgressLayout.progressLayout.visibility =
            if (b) View.VISIBLE else View.GONE
        binding.resourceStatusLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
    }

    private fun showError() {
        binding.resourceStatusLayout.resourceStatusContainer.visibility = View.VISIBLE
        binding.resourceStatusLayout.resourceErrorLayout.errorLayout.visibility = View.VISIBLE
        binding.resourceStatusLayout.resourceProgressLayout.progressLayout.visibility = View.GONE
    }
}