package com.revolgenx.anilib.app.setting.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.enableAutoMlTranslation
import com.revolgenx.anilib.common.preference.inUseMlLanguageModel
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.app.setting.data.model.MlLanguageModel
import com.revolgenx.anilib.databinding.MlLanguageChooserFragmentLayoutBinding
import com.revolgenx.anilib.app.setting.presenter.MlLanguagePresenter
import com.revolgenx.anilib.ui.view.makeConfirmationDialog
import com.revolgenx.anilib.ui.view.makeToast
import java.util.*

class MlLanguageChooserFragment : BaseToolbarFragment<MlLanguageChooserFragmentLayoutBinding>() {

    override var titleRes: Int? = R.string.available_languages
    override var setHomeAsUp: Boolean = true

    private val mlTranslateLanguageModels = mutableMapOf<String, MlLanguageModel>()

    private val adapter: Adapter by lazy {
        Adapter.builder(viewLifecycleOwner)
            .addPresenter(mlLanguagePresenter)
            .addSource(
                Source.fromList(
                    mlTranslateLanguageModels.values.toList().sortedWith(
                        compareBy({ !it.isInUse }, { !it.downloaded })
                    )
                )
            )
            .build()
    }

    private val mlLanguagePresenter by lazy {
        MlLanguagePresenter(requireContext(), { model ->
            if (context == null) {
                return@MlLanguagePresenter;
            }
            if (model.downloaded) {
                makeConfirmationDialog(requireContext()) {
                    val mlLanguageModel = TranslateRemoteModel.Builder(model.localeCode).build()
                    RemoteModelManager.getInstance()
                        .deleteDownloadedModel(mlLanguageModel)
                        .addOnSuccessListener {
                            context?.makeToast(R.string.done)
                            mlTranslateLanguageModels[model.localeCode]?.downloaded = false
                            if(model.isInUse){
                                mlTranslateLanguageModels[model.localeCode]?.isInUse = false
                                inUseMlLanguageModel(requireContext(), "")
                                enableAutoMlTranslation(requireContext(), false)
                            }
                            adapter.notifyDataSetChanged()
                        }
                        .addOnFailureListener {
                            context?.makeToast(
                                R.string.operation_failed,
                                icon = R.drawable.ic_error
                            )
                        }
                }

            } else {
                makeConfirmationDialog(requireContext(), messageRes = R.string.download_this_model) {
                    val mlLanguageModel = TranslateRemoteModel.Builder(model.localeCode).build()
                    RemoteModelManager.getInstance()
                        .download(mlLanguageModel, DownloadConditions.Builder().build())
                        .addOnSuccessListener {
                            context?.makeToast(R.string.done)
                            mlTranslateLanguageModels[model.localeCode]?.downloaded = true
                            adapter.notifyDataSetChanged()
                        }
                        .addOnFailureListener {
                            context?.makeToast(
                                R.string.operation_failed,
                                icon = R.drawable.ic_error
                            )
                        }
                }
            }
        }, { model ->
            if (context == null) return@MlLanguagePresenter
            if (model.isInUse) {
                makeConfirmationDialog(
                    requireContext(),
                    messageRes = R.string.remove_from_default
                ) {
                    inUseMlLanguageModel(requireContext(), "")
                    mlTranslateLanguageModels[model.localeCode]?.isInUse = false
                    enableAutoMlTranslation(requireContext(), false)
                    adapter.notifyDataSetChanged()
                }
            } else {
                if (!model.downloaded) {
                    requireContext().makeToast(R.string.download_the_language_model)
                } else {
                    makeConfirmationDialog(
                        requireContext(),
                        messageRes = R.string.user_as_default
                    ) {
                        inUseMlLanguageModel(requireContext(), model.localeCode)
                        mlTranslateLanguageModels.values.firstOrNull { it.isInUse }?.isInUse = false
                        mlTranslateLanguageModels[model.localeCode]?.isInUse = true
                        adapter.notifyDataSetChanged()
                    }

                }
            }
        })
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): MlLanguageChooserFragmentLayoutBinding {
        return MlLanguageChooserFragmentLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        TranslateLanguage.getAllLanguages().forEach {
            mlTranslateLanguageModels[it] = MlLanguageModel(it, Locale(it).displayLanguage)
        }

        showLoading(true)

        binding.mlLanguagesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        RemoteModelManager.getInstance().getDownloadedModels(TranslateRemoteModel::class.java)
            .addOnSuccessListener {
                it.forEach {
                    mlTranslateLanguageModels[it.language]!!.downloaded = true
                }
                val inUseMlLanguageModel = inUseMlLanguageModel(requireContext())
                if (inUseMlLanguageModel.isBlank().not()) {
                    mlTranslateLanguageModels[inUseMlLanguageModel]!!.isInUse = true
                }
                binding.mlLanguagesRecyclerView.adapter = adapter
                showLoading(false)
            }.addOnFailureListener {
                makeToast(R.string.failed_to_load, null, R.drawable.ads_ic_info)
            }

    }


    private fun showLoading(b: Boolean) {
        binding.mlLoadingBar.visibility = if (b) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        menu.findItem(R.id.base_search_menu)?.let { item ->
            (item.actionView as SearchView).also {
                it.setOnQueryTextListener(object :
                    SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {

                        return true
                    }
                })
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.base_search_menu -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
