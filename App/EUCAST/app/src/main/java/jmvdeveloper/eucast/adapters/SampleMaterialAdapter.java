package jmvdeveloper.eucast.adapters;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import jmvdeveloper.eucast.R;

public class SampleMaterialAdapter extends RecyclerView.Adapter<SampleMaterialAdapter.ViewHolder> {
    private static final String DEBUG_TAG = "SampleMaterialAdapter";

    public Context context;
    public ArrayList<String> nombres;

    public SampleMaterialAdapter(Context context, ArrayList<String> nombres) {
        this.context = context;
        this.nombres = nombres;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        String name = nombres.get(position);
        TextView nameTextView = viewHolder.name;
        nameTextView.setText(name);
    }

//    @Override
//    public void onViewDetachedFromWindow(ViewHolder viewHolder) {
//        super.onViewDetachedFromWindow(viewHolder);
//        viewHolder.itemView.clearAnimation();
//    }

//    @Override
//    public void onViewAttachedToWindow(ViewHolder viewHolder) {
//        super.onViewAttachedToWindow(viewHolder);
//        animateCircularReveal(viewHolder.itemView);
//    }

//    public void animateCircularReveal(View view) {
//        int centerX = 0;
//        int centerY = 0;
//        int startRadius = 0;
//        int endRadius = Math.max(view.getWidth(), view.getHeight());
//        Animator animation = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);
//        view.setVisibility(View.VISIBLE);
//        animation.start();
//    }

//    public void animateCircularDelete(final View view, final int list_position) {
//        int centerX = view.getWidth();
//        int centerY = view.getHeight();
//        int startRadius = view.getWidth();
//        int endRadius = 0;
//        Animator animation = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);
//
//        animation.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//
//                Log.d(DEBUG_TAG, "SampleMaterialAdapter onAnimationEnd for Edit adapter position " + list_position);
//                Log.d(DEBUG_TAG, "SampleMaterialAdapter onAnimationEnd for Edit cardId " + getItemId(list_position));
//
//                view.setVisibility(View.INVISIBLE);
//                nombres.remove(list_position);
//                notifyItemRemoved(list_position);
//            }
//        });
//        animation.start();
//    }

//    public void addCard(String name, int color) {
//        Card card = new Card();
//        card.setName(name);
//        card.setColorResource(color);
//        card.setId(getItemCount());
//        nombres.add(card);
////        ((SampleMaterialActivity) context).doSmoothScroll(getItemCount());
//        notifyItemInserted(getItemCount());
//    }

//    public void updateCard(String name, int list_position) {
//        nombres.get(list_position).setName(name);
//        Log.d(DEBUG_TAG, "list_position is " + list_position);
//        notifyItemChanged(list_position);
//    }
//
//    public void deleteCard(View view, int list_position) {
//        animateCircularDelete(view, list_position);
//    }

    @Override
    public int getItemCount() {
        if (nombres.isEmpty()) {
            return 0;
        } else {
            return nombres.size();
        }
    }

//    @Override
//    public long getItemId(int position) {
//        return nombres.get(position);
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater li = LayoutInflater.from(viewGroup.getContext());
        View v = li.inflate(R.layout.card_view_holder, viewGroup, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Pair<View, String> p1 = Pair.create((View) initial, SampleMaterialActivity.TRANSITION_INITIAL);
//                    Pair<View, String> p2 = Pair.create((View) name, SampleMaterialActivity.TRANSITION_NAME);
//                    Pair<View, String> p3 = Pair.create((View) deleteButton, SampleMaterialActivity.TRANSITION_DELETE_BUTTON);
//
//                    ActivityOptionsCompat options;
//                    Activity act = (AppCompatActivity) context;
//                    options = ActivityOptionsCompat.makeSceneTransitionAnimation(act, p1, p2, p3);
//
//                    int requestCode = getAdapterPosition();
//                    String name = nombres.get(requestCode).getName();
//                    int color = nombres.get(requestCode).getColorResource();
//
//                    Log.d(DEBUG_TAG, "SampleMaterialAdapter itemView listener for Edit adapter position " + requestCode);
//
//                    Intent transitionIntent = new Intent(context, TransitionEditActivity.class);
//                    transitionIntent.putExtra(SampleMaterialActivity.EXTRA_NAME, name);
//                    transitionIntent.putExtra(SampleMaterialActivity.EXTRA_INITIAL, Character.toString(name.charAt(0)));
//                    transitionIntent.putExtra(SampleMaterialActivity.EXTRA_COLOR, color);
//                    transitionIntent.putExtra(SampleMaterialActivity.EXTRA_UPDATE, false);
//                    transitionIntent.putExtra(SampleMaterialActivity.EXTRA_DELETE, false);
//                    ((AppCompatActivity) context).startActivityForResult(transitionIntent, requestCode, options.toBundle());
//                }
//            });
        }
    }
}
