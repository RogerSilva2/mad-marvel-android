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
import br.com.infinitytechnology.madmarvel.adapters.EventAdapter
import br.com.infinitytechnology.madmarvel.entities.Event
import br.com.infinitytechnology.madmarvel.entities.EventDataWrapper
import br.com.infinitytechnology.madmarvel.interfaces.EventsService
import br.com.infinitytechnology.madmarvel.utils.PropertyUtil
import br.com.infinitytechnology.madmarvel.utils.ServiceGenerator
import kotlinx.android.synthetic.main.fragment_events.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_TAG = "TAG"

class EventsFragment : Fragment(), View.OnClickListener {

    private var mProgressDialog: ProgressDialog? = null
    private var mTag: String? = null
    private val mEvents = ArrayList<Event>()

    private var listener: OnEventsFragmentInteractionListener? = null

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
        val view = inflater.inflate(R.layout.fragment_events, container, false)
        view.swipe_refresh_events.setColorSchemeResources(R.color.colorAccent)
        view.swipe_refresh_events.setOnRefreshListener { refreshList(view) }

        view.recycler_view_events.setHasFixedSize(true)
        view.recycler_view_events.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false)
        view.recycler_view_events.itemAnimator = DefaultItemAnimator()
        context?.let {
            view.recycler_view_events.adapter = EventAdapter(it, this, mEvents)
        }

        view.try_again_events.setOnClickListener {
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
            val service = ServiceGenerator.createService(it, EventsService::class.java)
            val eventsCall = service.events(ts, apiKey, hash, null,
                    null, null, null, null,
                    null, null, null, null, 20, 0)
            eventsCall.enqueue(object : Callback<EventDataWrapper> {
                override fun onResponse(call: Call<EventDataWrapper>,
                                        response: Response<EventDataWrapper>) {
                    if (response.isSuccessful) {
                        mEvents.clear()
                        response.body()?.data?.results?.let { it -> mEvents.addAll(it) }
                        refreshAdapter(view)
                    } else {
                        view.swipe_refresh_events.visibility = View.GONE
                        view.layout_connectivity_error_events.visibility = View.VISIBLE
                        view.swipe_refresh_events.isRefreshing = false
                        mProgressDialog?.hide()
                        Log.i(getString(R.string.app_name), getString(R.string.error_getting_characters))
                        showSnackbar(view, R.string.error_getting_characters)
                    }
                }

                override fun onFailure(call: Call<EventDataWrapper>, t: Throwable) {
                    view.swipe_refresh_events.visibility = View.GONE
                    view.layout_connectivity_error_events.visibility = View.VISIBLE
                    view.swipe_refresh_events.isRefreshing = false
                    mProgressDialog?.hide()
                    Log.e(getString(R.string.app_name), getString(R.string.error_server_unavailable), t)
                    showSnackbar(view, R.string.error_server_unavailable)
                }
            })
        }
    }

    private fun refreshAdapter(view: View) {
        view.layout_connectivity_error_events.visibility = View.GONE
        view.swipe_refresh_events.visibility = View.VISIBLE
        context?.let {
            view.recycler_view_events.adapter = EventAdapter(it, this, mEvents)
        }

        view.swipe_refresh_events.isRefreshing = false
        mProgressDialog?.hide()
    }

    private fun showSnackbar(view: View, @StringRes resId: Int) {
        Snackbar.make(view.recycler_view_events, resId, Snackbar.LENGTH_LONG).show()
    }

    override fun onClick(view: View) {
        val id = view.tag as Int
        onButtonPressed(mEvents[id])
    }

    private fun onButtonPressed(event: Event) {
        listener?.onEventsFragmentInteraction(event)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEventsFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() +
                    " must implement OnEventsFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnEventsFragmentInteractionListener {
        fun onEventsFragmentInteraction(event: Event)
    }

    companion object {
        @JvmStatic
        fun newInstance(tag: String) =
                EventsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_TAG, tag)
                    }
                }
    }
}