package stage.metasploit.com.backdooredapk;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Grunt on 15/02/2017.
 */

class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SMS> smsList;
    private RecyclerView rv;

    public void addSms(SMS sms) {
        smsList.add(sms);
        notifyItemInserted(smsList.size() - 1);
        rv.smoothScrollToPosition(smsList.size() - 1);
    }

    ConversationAdapter(List<SMS> smsList, RecyclerView rv) {
        this.smsList = smsList;
        this.rv = rv;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1)
            return new YourMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout_you, parent, false));
        return new MyMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout_me, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            YourMessageViewHolder yourMessageViewHolder = (YourMessageViewHolder) holder;
            yourMessageViewHolder.tvMessage.setText(smsList.get(position).getBody());
        } else {
            MyMessageViewHolder myMessageViewHolder = (MyMessageViewHolder)holder;
            myMessageViewHolder.tvMessage.setText(smsList.get(position).getBody());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (smsList.get(position).getType().compareTo("1") == 0)
            return 1;
        return 0;
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    static private class YourMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMessage;

        YourMessageViewHolder(View itemView) {
            super(itemView);
            this.tvMessage = (TextView) itemView.findViewById(R.id.text_message);
        }
    }

    static private class MyMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMessage;

        MyMessageViewHolder(View itemView) {
            super(itemView);
            this.tvMessage = (TextView) itemView.findViewById(R.id.text_message);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}