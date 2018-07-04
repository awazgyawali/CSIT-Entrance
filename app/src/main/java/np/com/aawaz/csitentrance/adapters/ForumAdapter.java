package np.com.aawaz.csitentrance.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.SupportActivity;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import np.com.aawaz.csitentrance.R;
import np.com.aawaz.csitentrance.activities.CommentsActivity;
import np.com.aawaz.csitentrance.activities.ProfileActivity;
import np.com.aawaz.csitentrance.interfaces.ClickListener;
import np.com.aawaz.csitentrance.misc.MyApplication;
import np.com.aawaz.csitentrance.objects.Post;
import np.com.aawaz.csitentrance.objects.SPHandler;


public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ViewHolder> {

    private SupportActivity context;
    private LayoutInflater inflater;
    private boolean fromForum;
    private ArrayList<Post> posts = new ArrayList<>();

    private ClickListener clickListener;


    public ForumAdapter(SupportActivity context, boolean fromForum) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fromForum = fromForum;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.each_feed_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Post post = posts.get(holder.getAdapterPosition());
        holder.postedBy.setText(post.getAuthor());
        holder.realPost.setText(post.getMessage());
        holder.commentCount.setText(post.getComment_count() + getCommentMessage(post));
        holder.commentCount.setPaintFlags(holder.commentCount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.time.setText(getRelativeTimeSpanString(post.getTime_stamp()));

        if (SPHandler.containsDevUID(post.getUid()))
            holder.postedBy.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.admin, 0);
        else
            holder.postedBy.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(post.getUid()))
            holder.editIcon.setVisibility(View.VISIBLE);
        else
            holder.editIcon.setVisibility(View.GONE);

        try {
            Picasso.with(MyApplication.getAppContext())
                    .load(post.getImage_url())
                    .error(TextDrawable.builder().buildRound(String.valueOf(post.getAuthor().charAt(0)).toUpperCase(), Color.BLUE))
                    .placeholder(TextDrawable.builder().buildRound(String.valueOf(post.getAuthor().charAt(0)).toUpperCase(), Color.BLUE))
                    .into(holder.profile);
        } catch (Exception e) {
            holder.profile.setImageDrawable(TextDrawable.builder().buildRound(String.valueOf(post.getAuthor().charAt(0)).toUpperCase(), Color.BLUE));
        }

        holder.editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null)
                    clickListener.itemLongClicked(view, position);
            }
        });

        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromForum)
                    context.startActivity(new Intent(context, ProfileActivity.class).putExtra("uid", post.getUid()));
            }
        });
    }

    private CharSequence getRelativeTimeSpanString(long time_stamp) {
        return DateUtils.getRelativeTimeSpanString(time_stamp, new Date().getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void addToTop(Post post, String key) {
        post.setKey(key);
        posts.add(0, post);
        notifyItemInserted(0);
    }

    public void removeItemAtPosition(int i) {
        posts.remove(i);
        notifyItemRemoved(i);
    }

    public void editItemAtPosition(int i) {
        notifyItemChanged(i);
    }

    public void editItemAtPosition(Post post, int i) {
        posts.remove(i);
        posts.add(i, post);
        notifyItemChanged(i);
    }

    public Post getItemAtPosition(int i) {
        return posts.get(i);
    }

    public String getUidAt(int position) {
        return posts.get(position).getUid();
    }

    public int getCommentCount(int position) {
        return posts.get(position).getComment_count();
    }

    public String getMessageAt(int position) {
        return posts.get(position).getMessage();
    }

    public String getKeyAt(int position) {
        return posts.get(position).getKey();
    }

    private String getCommentMessage(Post post) {
        return post.getComment_count() == 1 ? " comment" : " comments";
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView commentCount, realPost, postedBy, time;
        ImageView editIcon, call;
        CircleImageView profile;
        View core;

        ViewHolder(View itemView) {
            super(itemView);
            core = itemView;
            commentCount = itemView.findViewById(R.id.commentCount);
            call = itemView.findViewById(R.id.call_orchid);
            realPost = itemView.findViewById(R.id.realPost);
            postedBy = itemView.findViewById(R.id.postedBy);
            time = itemView.findViewById(R.id.forumTime);
            editIcon = itemView.findViewById(R.id.editIcon);
            profile = itemView.findViewById(R.id.forumPic);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null)
                        clickListener.itemClicked(view, getAdapterPosition());
                    else {
                        context.startActivity(new Intent(context, CommentsActivity.class)
                                .putExtra("key", getKeyAt(getAdapterPosition()))
                                .putExtra("message", getMessageAt(getAdapterPosition()))
                                .putExtra("comment_count", getCommentCount(getAdapterPosition())));
                    }

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (clickListener != null)
                        clickListener.itemLongClicked(v, getAdapterPosition());
                    return true;
                }
            });
        }
    }
}