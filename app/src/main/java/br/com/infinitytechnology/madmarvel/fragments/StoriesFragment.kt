package br.com.infinitytechnology.madmarvel.fragments

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.com.infinitytechnology.madmarvel.R
import br.com.infinitytechnology.madmarvel.adapters.StoryAdapter
import br.com.infinitytechnology.madmarvel.entities.Story
import br.com.infinitytechnology.madmarvel.entities.StoryDataWrapper
import br.com.infinitytechnology.madmarvel.interfaces.StoriesService
import br.com.infinitytechnology.madmarvel.utils.PropertyUtil
import br.com.infinitytechnology.madmarvel.utils.ServiceGenerator
import kotlinx.android.synthetic.main.fragment_stories.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_TAG = "TAG"

class StoriesFragment : Fragment(), View.OnClickListener {

    private var mProgressDialog: ProgressDialog? = null
    private var mTag: String? = null
    private val mStories = ArrayList<Story>()

    private var listener: OnStoriesFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mTag = it.getString(ARG_TAG)
        }
        savedInstanceState?.let {
            mTag = it.getString(ARG_TAG)
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putString(ARG_TAG, mTag)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stories, container, false)
        view.swipe_refresh_stories.setColorSchemeResources(R.color.colorAccent)
        view.swipe_refresh_stories.setOnRefreshListener { refreshList(view) }

        view.recycler_view_stories.setHasFixedSize(true)
        view.recycler_view_stories.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false)
        view.recycler_view_stories.itemAnimator = DefaultItemAnimator()
        context?.let {
            view.recycler_view_stories.adapter = StoryAdapter(it, this, mStories)
        }

        view.try_again_stories.setOnClickListener {
            mProgressDialog?.show()
            refreshList(view)
        }

        mProgressDialog = ProgressDialog(activity)
        mProgressDialog?.let {
            it.setTitle("")
            it.setMessage(getString(R.string.message_loading))
            it.isIndeterminate = true
            it.setCancelable(false)
            it.show()
        }

        refreshList(view)
        return view
    }

    private fun refreshList(view: View) {
        context?.let {
            val ts = PropertyUtil.property(it, "ts")
            val apiKey = PropertyUtil.property(it, "api.key")
            val hash = PropertyUtil.property(it, "hash")
            val service = ServiceGenerator.createService(it, StoriesService::class.java)
            val storiesCall = service.stories(ts, apiKey, hash, null,
                    null, null, null, null, null,
                    null, 20, 0)
            storiesCall.enqueue(object : Callback<StoryDataWrapper> {
                override fun onResponse(call: Call<StoryDataWrapper>,
                                        response: Response<StoryDataWrapper>) {
                    if (response.isSuccessful) {
                        mStories.clear()
                        response.body()?.data?.results?.let { it -> mStories.addAll(it) }
                        refreshAdapter(view)
                    } else {
                        view.swipe_refresh_stories.visibility = View.GONE
                        view.layout_connectivity_error_stories.visibility = View.VISIBLE
                        view.swipe_refresh_stories.isRefreshing = false
                        mProgressDialog?.hide()
                        Log.i(getString(R.string.app_name), getString(R.string.error_getting_characters))
                        showSnackbar(view, R.string.error_getting_characters)
                    }
                }

                override fun onFailure(call: Call<StoryDataWrapper>, t: Throwable) {
                    view.swipe_refresh_stories.visibility = View.GONE
                    view.layout_connectivity_error_stories.visibility = View.VISIBLE
                    view.swipe_refresh_stories.isRefreshing = false
                    mProgressDialog?.hide()
                    Log.e(getString(R.string.app_name), getString(R.string.error_server_unavailable), t)
                    showSnackbar(view, R.string.error_server_unavailable)
                }
            })
        }
    }

    private fun refreshAdapter(view: View) {
        view.layout_connectivity_error_stories.visibility = View.GONE
        view.swipe_refresh_stories.visibility = View.VISIBLE
        context?.let {
            view.recycler_view_stories.adapter = StoryAdapter(it, this, mStories)
        }

        view.swipe_refresh_stories.isRefreshing = false
        mProgressDialog?.hide()
    }

    private fun showSnackbar(view: View, @StringRes resId: Int) {
        Snackbar.make(view.recycler_view_stories, resId, Snackbar.LENGTH_LONG).show()
    }

    override fun onClick(view: View) {
        val id = view.tag as Int
        onButtonPressed(mStories[id])
    }

    fun onButtonPressed(story: Story) {
        listener?.onStoriesFragmentInteraction(story)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnStoriesFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnStoriesFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnStoriesFragmentInteractionListener {
        fun onStoriesFragmentInteraction(story: Story)
    }

    companion object {
        @JvmStatic
        fun newInstance(tag: String) =
                StoriesFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_TAG, tag)
                    }
                }
    }
}