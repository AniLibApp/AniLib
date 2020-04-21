package com.revolgenx.anilib.fragment.home

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.DividerItemDecoration
import com.revolgenx.anilib.R
import com.revolgenx.anilib.event.BrowseEvent
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.markwon.MarkwonImpl
import com.revolgenx.anilib.preference.loggedIn
import com.revolgenx.anilib.util.makeSnakeBar
import io.noties.markwon.recycler.MarkwonAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.discover_fragment_layout.*


class DiscoverFragment : BaseFragment() {
    val makrdowntest =
        "<img width='100%'  src='https://dream-wonderland.com/blog/wp-content/uploads/2017/08/Ohys-Raws-Aikatsu-Stars-69-TX-1280x720-x264-AAC.mp4_20170820_005650.721.jpg'>"

    val test =
        "<a href=\"https://anilist.co/forum/thread/6715/comment/148155\" target=\"_blank\"><span><span class='markdown_spoiler'><img width='1080' alt='markdown_spoiler' src='https://media1.tenor.com/images/fcdbd7e6438f73799ba0c0704b44daa6/tenor.gif?itemid=3558286'></span></span></a>"+
    "\norem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n"

    val youtube = "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'><p>Youtube</p></div>"

    val test1 = "**Hello** world <p>hello</p>" +        "<img width='1080'  src='https://media1.tenor.com/images/fcdbd7e6438f73799ba0c0704b44daa6/tenor.gif?itemid=3558286'>"
    val video =
        "<video muted loop autoplay controls><source src='https://media1.tenor.com/images/fcdbd7e6438f73799ba0c0704b44daa6/tenor.gif?itemid=3558286' type='video/webm'>https://media1.tenor.com/images/fcdbd7e6438f73799ba0c0704b44daa6/tenor.gif?itemid=3558286</video>"


    val combined = "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
            "\n" +
            "Why do we use it?\n" +
            "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n" +
            "\norem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
            "\n" +
            "Why do we use it?\n" +
            "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n" +
            "\norem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
            "\n" +
            "Why do we use it?\n" +
            "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n" +
            "\norem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
            "\n" +
            "Why do we use it? <img width='440'  src='https://dream-wonderland.com/blog/wp-content/uploads/2017/08/Ohys-Raws-Aikatsu-Stars-69-TX-1280x720-x264-AAC.mp4_20170820_005650.721.jpg'>"+
    "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n" +
            "\n<video muted loop autoplay alt='markdown_spoiler' controls><source src='https://media1.tenor.com/images/fcdbd7e6438f73799ba0c0704b44daa6/tenor.gif?itemid=3558286' type='video/webm'>video###https://media1.tenor.com/images/fcdbd7e6438f73799ba0c0704b44daa6/tenor.gif?itemid=3558286</video>\n" +
            "<span class='markdown_spoiler'><span><div class='youtube' alt='markdown_spoiler' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'><p>youtube###https://www.youtube.com/watch?v=XoyLbuX8EXU</p></div></span></span>\n"
/*https://files.catbox.moe/0zofnv.mp4*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.discover_fragment_layout, container, false)
    }


    override fun onResume() {
        super.onResume()
        invalidateOptionMenu()
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_editor_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.listFavMenu -> {
                activity?.drawerLayout?.openDrawer(activity?.mainBrowseFilterNavView!!, true)
                true
            }
            R.id.listDeleteMenu -> {
                BrowseEvent().postEvent
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = MarkwonAdapter.create(R.layout.markwon_textview_layout, R.id.markdown_holder_tv)
        markwonRecyclerView.adapter = adapter
        markwonRecyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        adapter.setMarkdown(
            MarkwonImpl.createHtmlInstance(requireContext()),test
        )
        adapter.notifyDataSetChanged()
    }


    /*            "<span></span>\n\n hello \n\n hello\n\n<span></span><p>a</p> **hello** <h1>2</h1> <div> __hello__# hello # hello <h1></h1># hello# hello<h1></h1># hello</div>"*/

    private fun View.checkLoggedIn(): Boolean {
        val loggedIn = context.loggedIn()
        if (!loggedIn) makeSnakeBar(R.string.please_log_in)
        return loggedIn
    }

}
