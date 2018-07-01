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
import br.com.infinitytechnology.madmarvel.adapters.CharacterAdapter
import br.com.infinitytechnology.madmarvel.entities.Character
import br.com.infinitytechnology.madmarvel.entities.CharacterDataWrapper
import br.com.infinitytechnology.madmarvel.interfaces.CharactersService
import br.com.infinitytechnology.madmarvel.utils.PropertyUtil
import br.com.infinitytechnology.madmarvel.utils.ServiceGenerator
import kotlinx.android.synthetic.main.fragment_characters.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_TAG = "TAG"

class CharactersFragment : Fragment(), View.OnClickListener {

    private var mProgressDialog: ProgressDialog? = null
    private var mTag: String? = null
    private val mCharacters = ArrayList<Character>()

    private var listener: OnCharactersFragmentInteractionListener? = null

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
        val view = inflater.inflate(R.layout.fragment_characters, container, false)
        view.swipe_refresh_characters.setColorSchemeResources(R.color.colorAccent)
        view.swipe_refresh_characters.setOnRefreshListener { refreshList(view) }

        view.recycler_view_characters.setHasFixedSize(true)
        view.recycler_view_characters.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false)
        view.recycler_view_characters.itemAnimator = DefaultItemAnimator()
        context?.let {
            view.recycler_view_characters.adapter = CharacterAdapter(it, this, mCharacters)
        }

        view.try_again.setOnClickListener {
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
            val service = ServiceGenerator.createService(it, CharactersService::class.java)
            val charactersCall = service.characters(ts, apiKey, hash, null,
                    null, null, null, null, null,
                    null, null, null, null)
            charactersCall.enqueue(object : Callback<CharacterDataWrapper> {
                override fun onResponse(call: Call<CharacterDataWrapper>,
                               response: Response<CharacterDataWrapper>) {
                    if (response.isSuccessful) {
                        mCharacters.clear()
                        response.body()?.data?.results?.let { mCharacters.addAll(it) }
                        refreshAdapter(view)
                    } else {
                        view.swipe_refresh_characters.visibility = View.GONE
                        view.layout_connectivity_error.visibility = View.VISIBLE
                        view.swipe_refresh_characters.isRefreshing = false
                        mProgressDialog?.hide()
                        Log.i(getString(R.string.app_name), getString(R.string.error_getting_characters))
                        showSnackbar(view, R.string.error_getting_characters)
                    }
                }

                override fun onFailure(call: Call<CharacterDataWrapper>, t: Throwable) {
                    view.swipe_refresh_characters.visibility = View.GONE
                    view.layout_connectivity_error.visibility = View.VISIBLE
                    view.swipe_refresh_characters.isRefreshing = false
                    mProgressDialog?.hide()
                    Log.e(getString(R.string.app_name), getString(R.string.error_server_unavailable), t)
                    showSnackbar(view, R.string.error_server_unavailable)
                }
            })
        }
    }

    private fun refreshAdapter(view: View) {
        view.layout_connectivity_error.visibility = View.GONE
        view.swipe_refresh_characters.visibility = View.VISIBLE
        context?.let {
            view.recycler_view_characters.adapter = CharacterAdapter(it, this, mCharacters)
        }

        view.swipe_refresh_characters.isRefreshing = false
        mProgressDialog?.hide()
    }

    private fun showSnackbar(view: View, @StringRes resId: Int) {
        Snackbar.make(view.recycler_view_characters, resId, Snackbar.LENGTH_LONG).show()
    }

    override fun onClick(view: View) {
        val id = view.tag as Int
        onButtonPressed(mCharacters[id])
    }

    private fun onButtonPressed(character: Character) {
        listener?.onCharactersFragmentInteraction(character)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCharactersFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnCharactersFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnCharactersFragmentInteractionListener {
        fun onCharactersFragmentInteraction(character: Character)
    }

    companion object {
        @JvmStatic
        fun newInstance(tag: String) =
                CharactersFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_TAG, tag)
                    }
                }
    }
}