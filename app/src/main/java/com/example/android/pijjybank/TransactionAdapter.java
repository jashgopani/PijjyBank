package com.example.android.pijjybank;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ExpenseViewHolder> {

    private Context mCtx;
    private List<Transaction> transactionList;
    private OnItemClickListener mListener;

    public TransactionAdapter(Context mCtx, ArrayList<Transaction> transactionList) {
        this.mCtx = mCtx;
        this.transactionList = transactionList;
    }

    /*
     * the ExpenseViewHolder method will return an instance of the ExpeneViewHolder class
     * basically it will provide us the elements to be displayed
     */
    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_item, parent, false);
        ExpenseViewHolder holder = new ExpenseViewHolder(view);
        return holder;
    }

    /*
     * this function will bind the data to our data UI elements*/
    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position); //to get the transaction at current position
        holder.titleTextView.setText(transaction.getTitle());
        holder.imageview.setImageResource(transaction.categoryIcon);
        holder.dateTextView.setText(transaction.getDate());
        holder.amountTextView.setText(transaction.getAmount());

//        final String x = holder.titleTextView.getText().toString();
//        holder.itemCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mCtx, "Clicked "+x, Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    //Interface for click listener on single item
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    class ExpenseViewHolder extends RecyclerView.ViewHolder {

        ImageView imageview;
        TextView titleTextView, dateTextView, amountTextView;
        CardView itemCard;


        public ExpenseViewHolder(View itemView) {
            super(itemView);
            itemCard = itemView.findViewById(R.id.cardItemTimeline);
            imageview = itemView.findViewById(R.id.category_image_view);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            amountTextView = itemView.findViewById(R.id.amount_text_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
