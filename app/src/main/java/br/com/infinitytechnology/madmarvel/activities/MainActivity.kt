package br.com.infinitytechnology.madmarvel.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import br.com.infinitytechnology.madmarvel.R
import br.com.infinitytechnology.madmarvel.entities.Character
import br.com.infinitytechnology.madmarvel.entities.Comic
import br.com.infinitytechnology.madmarvel.entities.Creator
import br.com.infinitytechnology.madmarvel.entities.Event
import br.com.infinitytechnology.madmarvel.fragments.CharactersFragment
import br.com.infinitytechnology.madmarvel.fragments.ComicsFragment
import br.com.infinitytechnology.madmarvel.fragments.CreatorsFragment
import br.com.infinitytechnology.madmarvel.fragments.EventsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

private const val TAG_FRAGMENT_CHARACTERS = "FRAGMENT_CHARACTERS"
private const val TAG_FRAGMENT_COMICS = "FRAGMENT_COMICS"
private const val TAG_FRAGMENT_CREATORS = "FRAGMENT_CREATORS"
private const val TAG_FRAGMENT_EVENTS = "FRAGMENT_EVENTS"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        CharactersFragment.OnCharactersFragmentInteractionListener,
        ComicsFragment.OnComicsFragmentInteractionListener,
        CreatorsFragment.OnCreatorsFragmentInteractionListener,
        EventsFragment.OnEventsFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        commitCharactersFragment(TAG_FRAGMENT_CHARACTERS)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_acknowledgments -> {
                val intent = Intent(this, AcknowledgmentsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        unCheckAllMenuItems(nav_view.menu)
        item.isChecked = true

        when (item.itemId) {
            R.id.nav_characters -> {
                commitCharactersFragment(TAG_FRAGMENT_CHARACTERS)
            }
            R.id.nav_comics -> {
                commitComicsFragment(TAG_FRAGMENT_COMICS)
            }
            R.id.nav_creators -> {
                commitCreatorsFragment(TAG_FRAGMENT_CREATORS)
            }
            R.id.nav_events -> {
                commitEventsFragment(TAG_FRAGMENT_EVENTS)
            }
            R.id.nav_series -> {
            }
            R.id.nav_stories -> {
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun unCheckAllMenuItems(menu: Menu) {
        val size = menu.size()
        for (i in 0 until size) {
            val item = menu.getItem(i)
            if (item.hasSubMenu()) {
                unCheckAllMenuItems(item.subMenu)
            } else {
                item.isChecked = false
            }
        }
    }

    private fun commitCharactersFragment(tag: String) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.container, CharactersFragment.newInstance(tag), tag)
                .commit()
    }

    private fun commitComicsFragment(tag: String) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.container, ComicsFragment.newInstance(tag), tag)
                .commit()
    }

    private fun commitCreatorsFragment(tag: String) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.container, CreatorsFragment.newInstance(tag), tag)
                .commit()
    }

    private fun commitEventsFragment(tag: String) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.container, EventsFragment.newInstance(tag), tag)
                .commit()
    }

    override fun onCharactersFragmentInteraction(character: Character) {
    }

    override fun onComicsFragmentInteraction(comic: Comic) {
    }

    override fun onCreatorsFragmentInteraction(creator: Creator) {
    }

    override fun onEventsFragmentInteraction(event: Event) {
    }
}