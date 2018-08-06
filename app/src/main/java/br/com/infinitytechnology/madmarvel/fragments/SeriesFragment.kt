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
import br.com.infinitytechnology.madmarvel.adapters.SeriesAdapter
import br.com.infinitytechnology.madmarvel.entities.Series
import br.com.infinitytechnology.madmarvel.entities.SeriesDataWrapper
import br.com.infinitytechnology.madmarvel.interfaces.SeriesService
import br.com.infinitytechnology.madmarvel.utils.PropertyUtil
import br.com.infinitytechnology.madmarvel.utils.ServiceGenerator
import kotlinx.android.synthetic.main.fragment_series.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_TAG = "TAG"

class SeriesFragment : Fragment(), View.OnClickListener {

    private var mProgressDialog: ProgressDialog? = null
    private var mTag: String? = null
    private val mSeries = ArrayList<Series>()

    private var listener: OnSeriesFragmentInteractionListener? = null

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
        val view = inflater.inflate(R.layout.fragment_series, container, false)
        view.swipe_refresh_series.setColorSchemeResources(R.color.colorAccent)
        view.swipe_refresh_series.setOnRefreshListener { refreshList(view) }

        view.recycler_view_series.setHasFixedSize(true)
        view.recycler_view_series.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false)
        view.recycler_view_series.itemAnimator = DefaultItemAnimator()
        context?.let {
            view.recycler_view_series.adapter = SeriesAdapter(it, this, mSeries)
        }

        view.try_again_series.setOnClickListener {
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
            val service = ServiceGenerator.createService(it, SeriesService::class.java)
            val seriesCall = service.series(ts, apiKey, hash, null,
                    null, null, null, null, null,
                    null, null, null, null, null,
                    null, null, null)
            seriesCall.enqueue(object : Callback<SeriesDataWrapper> {
                override fun onResponse(call: Call<SeriesDataWrapper>,
                                        response: Response<SeriesDataWrapper>) {
                    if (response.isSuccessful) {
                        mSeries.clear()
                        response.body()?.data?.results?.let { it -> mSeries.addAll(it) }
                        refreshAdapter(view)
                    } else {
                        view.swipe_refresh_series.visibility = View.GONE
                        view.layout_connectivity_error_series.visibility = View.VISIBLE
                        view.swipe_refresh_series.isRefreshing = false
                        mProgressDialog?.hide()
                        Log.i(getString(R.string.app_name), getString(R.string.error_getting_characters))
                        showSnackbar(view, R.string.error_getting_characters)
                    }
                }

                override fun onFailure(call: Call<SeriesDataWrapper>, t: Throwable) {
                    view.swipe_refresh_series.visibility = View.GONE
                    view.layout_connectivity_error_series.visibility = View.VISIBLE
                    view.swipe_refresh_series.isRefreshing = false
                    mProgressDialog?.hide()
                    Log.e(getString(R.string.app_name), getString(R.string.error_server_unavailable), t)
                    showSnackbar(view, R.string.error_server_unavailable)
                }
            })
        }
    }

    private fun refreshAdapter(view: View) {
        view.layout_connectivity_error_series.visibility = View.GONE
        view.swipe_refresh_series.visibility = View.VISIBLE
        context?.let {
            view.recycler_view_series.adapter = SeriesAdapter(it, this, mSeries)
        }

        view.swipe_refresh_series.isRefreshing = false
        mProgressDialog?.hide()
    }

    private fun showSnackbar(view: View, @StringRes resId: Int) {
        Snackbar.make(view.recycler_view_series, resId, Snackbar.LENGTH_LONG).show()
    }

    override fun onClick(view: View) {
        val id = view.tag as Int
        onButtonPressed(mSeries[id])
    }

    private fun onButtonPressed(series: Series) {
        listener?.onSeriesFragmentInteraction(series)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSeriesFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() +
                    " must implement OnSeriesFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnSeriesFragmentInteractionListener {
        fun onSeriesFragmentInteraction(series: Series)
    }

    companion object {
        @JvmStatic
        fun newInstance(tag: String) =
                SeriesFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_TAG, tag)
                    }
                }
    }
}