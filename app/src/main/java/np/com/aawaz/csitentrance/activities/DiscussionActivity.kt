package np.com.aawaz.csitentrance.activities

import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_discussion.*
import np.com.aawaz.csitentrance.R
import np.com.aawaz.csitentrance.adapters.CommentAdapter
import np.com.aawaz.csitentrance.interfaces.ClickListener
import np.com.aawaz.csitentrance.misc.Singleton
import np.com.aawaz.csitentrance.objects.Comment
import np.com.aawaz.csitentrance.objects.EventSender
import np.com.aawaz.csitentrance.objects.SPHandler
import java.util.*
import kotlin.collections.ArrayList


class DiscussionActivity : AppCompatActivity(), ChildEventListener {


    internal lateinit var adapter: CommentAdapter
    internal lateinit var reference: DatabaseReference
    internal var key: ArrayList<String> = ArrayList()


    private fun readyCommentButton() {
        EventSender().logEvent("opened_comments")

        addCommentTextDiscussion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                commentDiscussionButton.isEnabled = charSequence.isNotEmpty()
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
        commentDiscussionButton.setOnClickListener {
            if (addCommentTextDiscussion.text.toString().isNotEmpty())
                sendPostRequestThroughGraph(addCommentTextDiscussion.text.toString())
            else
                Toast.makeText(this, "Comment cannot be empty.", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onStop() {
        reference.removeEventListener(this)
        super.onStop()
    }

    private fun sendPostRequestThroughGraph(message: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        val comment = Comment(currentUser!!.uid, currentUser.displayName, System.currentTimeMillis(), message, currentUser.photoUrl!!.toString())
        val postValues = comment.toMap()

        reference.push().setValue(postValues)
        addCommentTextDiscussion.setText("")
        commentsRecyDiscussion.scrollToPosition(adapter.itemCount - 1)
    }

    private fun fillViews() {
        commentsRecyDiscussion.layoutManager = LinearLayoutManager(this)
        adapter = CommentAdapter(this, false)
        commentsRecyDiscussion.adapter = adapter
        adapter.setClickEventListener(object : ClickListener {
            override fun itemClicked(view: View, position: Int) {

            }

            override fun itemLongClicked(view: View, position: Int) {
                if (FirebaseAuth.getInstance().currentUser!!.uid == adapter.getUidAt(position) || SPHandler.containsDevUID(FirebaseAuth.getInstance().currentUser!!.uid)) {
                    MaterialDialog.Builder(this@DiscussionActivity)
                            .title("Select any option")
                            .items("Edit", "Delete")
                            .itemsCallback { dialog, itemView, which, text ->
                                if (which == 0)
                                    showDialogToEdit(adapter.getMessageAt(position), position)
                                else if (which == 1) {
                                    reference.child(key[position]).removeValue()
                                }
                            }
                            .build()
                            .show()
                }
            }

            override fun upVoteClicked(view: View, position: Int) {
            }
        })

        readyCommentButton()

        addListener()

    }

    private fun showDialogToEdit(message: String, position: Int) {
        val dialog = MaterialDialog.Builder(this)
                .title("Edit post")
                .input("Your message", message, false) { dialog, input ->
                    val map = HashMap<String, Any>()
                    map["message"] = input.toString()
                    reference.child(key[position]).updateChildren(map)
                }
                .positiveText("Save")
                .build()

        dialog.inputEditText!!.setLines(5)
        dialog.inputEditText!!.setSingleLine(false)
        dialog.inputEditText!!.maxLines = 7
        dialog.show()
    }


    private fun addListener() {
        reference.addChildEventListener(this)
    }


    override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
        val newPost = dataSnapshot.getValue(Comment::class.java)
        adapter.addToTop(newPost)
        key.add(dataSnapshot.key!!)
        errorDiscussion.visibility = View.GONE
    }

    override fun onChildChanged(snapshot: DataSnapshot, k: String?) {
        for (i in 0..(key.size - 1)) {
            if (snapshot.key == key[i])
                adapter.editItemAtPosition(i, snapshot.getValue(Comment::class.java))
        }

    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        for (i in 0..(key.size - 1)) {
            if (snapshot.key == key[i]) {
                adapter.removeItemAtPosition(i)
                key.removeAt(i)
            }
        }
    }


    override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {}

    override fun onCancelled(databaseError: DatabaseError) {
        errorDiscussion.visibility = View.VISIBLE
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discussion)


        val toolbar = findViewById<View>(R.id.toolbarDiscussion) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Discussion"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        }

        val rootReference = FirebaseDatabase.getInstance().reference
        reference = rootReference.child("discussion/comments").child("${intent.getIntExtra("code", 0)}-${intent.getIntExtra("position", 0)}")

        fillPost()

        fillViews()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fillPost() {
        val question = Singleton.getInstance().getQuestionAt(this, intent.getIntExtra("code", 0), intent.getIntExtra("position", 0))
        val htmlData: String = "<html lang=\"en\">" +
                "<head>" +
                "  <meta charset=\"UTF-8\">" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">" +
                "  <link href=\"https://fonts.googleapis.com/css?family=Work+Sans:400,700\" rel=\"stylesheet\">" +
                "  <title>Document</title>" +
                "  <style>" +
                "    body{" +
                "      margin: 0;" +
                "    }" +
                "    div {" +
                "      font-size: 18px;" +
                "      line-height: 1.5em;" +
                "      text-align: justify;" +
                "      font-family: 'Work Sans', sans-serif" +
                "    }" +
                "  </style>" +
                "</head>" +
                "<body>" +
                "<div>" + question.question +
                "<br>" + "a) " + question.a +
                "<br>" + "b) " + question.b +
                "<br>" + "c) " + question.c +
                "<br>" + "d) " + question.d +
                "<br>" + "<b>Answer: " +
                question.ans +
                "</b><br>" +
                "</div>" +
                "</body>" +
                "</html>"

        questionViewDiscussion.loadDataWithBaseURL("", htmlData, "text/html", "utf-8", null)
    }
}