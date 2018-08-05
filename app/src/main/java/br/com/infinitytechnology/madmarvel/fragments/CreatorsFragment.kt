package br.com.infinitytechnology.madmarvel.fragments

import android.app.ProgressDialog
import android.content.Context
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
import br.com.infinitytechnology.madmarvel.adapters.CreatorAdapter
import br.com.infinitytechnology.madmarvel.entities.Creator
import br.com.infinitytechnology.madmarvel.entities.CreatorDataWrapper
import br.com.infinitytechnology.madmarvel.interfaces.CreatorsService
import br.com.infinitytechnology.madmarvel.utils.PropertyUtil
import br.com.infinitytechnology.madmarvel.utils.ServiceGenerator
import kotlinx.android.synthetic.main.fragment_creators.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_TAG = "TAG"

class CreatorsFragment : Fragment(), View.OnClickListener {

    private var mProgressDialog: ProgressDialog? = null
    private var mTag: String? = null
    private val mCreators = ArrayList<Creator>()

    private var listener: OnCreatorsFragmentInteractionListener? = null

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
        val view = inflater.inflate(R.layout.fragment_creators, container, false)
        view.swipe_refresh_creators.setColorSchemeResources(R.color.colorAccent)
        view.swipe_refresh_creators.setOnRefreshListener { refreshList(view) }

        view.recycler_view_creators.setHasFixedSize(true)
        view.recycler_view_creators.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false)
        view.recycler_view_creators.itemAnimator = DefaultItemAnimator()
        context?.let {
            view.recycler_view_creators.adapter = CreatorAdapter(it, this, mCreators)
        }

        view.try_again_creators.setOnClickListener {
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
            val service = ServiceGenerator.createService(it, CreatorsService::class.java)
            val creatorsCall = service.creators(ts, apiKey, hash, null,
                    null, null, null, null, null,
                    null, null, null, null,
                    null, null, null, null, null, null)
            creatorsCall.enqueue(object : Callback<CreatorDataWrapper> {
                override fun onResponse(call: Call<CreatorDataWrapper>,
                                        response: Response<CreatorDataWrapper>) {
                    if (response.isSuccessful) {
                        mCreators.clear()
                        response.body()?.data?.results?.let { mCreators.addAll(it) }
                        refreshAdapter(view)
                    } else {
                        view.swipe_refresh_creators.visibility = View.GONE
                        view.layout_connectivity_error_creators.visibility = View.VISIBLE
                        view.swipe_refresh_creators.isRefreshing = false
                        mProgressDialog?.hide()
                        Log.i(getString(R.string.app_name), getString(R.string.error_getting_characters))
                        showSnackbar(view, R.string.error_getting_characters)
                    }
                }

                override fun onFailure(call: Call<CreatorDataWrapper>, t: Throwable) {
                    view.swipe_refresh_creators.visibility = View.GONE
                    view.layout_connectivity_error_creators.visibility = View.VISIBLE
                    view.swipe_refresh_creators.isRefreshing = false
                    mProgressDialog?.hide()
                    Log.e(getString(R.string.app_name), getString(R.string.error_server_unavailable), t)
                    showSnackbar(view, R.string.error_server_unavailable)
                }
            })
        }
    }

    private fun refreshAdapter(view: View) {
        view.layout_connectivity_error_creators.visibility = View.GONE
        view.swipe_refresh_creators.visibility = View.VISIBLE
        context?.let {
            view.recycler_view_creators.adapter = CreatorAdapter(it, this, mCreators)
        }

        view.swipe_refresh_creators.isRefreshing = false
        mProgressDialog?.hide()
    }

    private fun showSnackbar(view: View, @StringRes resId: Int) {
        Snackbar.make(view.recycler_view_creators, resId, Snackbar.LENGTH_LONG).show()
    }

    override fun onClick(view: View) {
        val id = view.tag as Int
        onButtonPressed(mCreators[id])
    }

    private fun onButtonPressed(creator: Creator) {
        listener?.onCreatorsFragmentInteraction(creator)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCreatorsFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnCreatorsFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnCreatorsFragmentInteractionListener {
        fun onCreatorsFragmentInteraction(creator: Creator)
    }

    companion object {
        @JvmStatic
        fun newInstance(tag: String) =
                CreatorsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_TAG, tag)
                    }
                }
    }
}