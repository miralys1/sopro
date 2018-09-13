package swarm.swarmcomposerapp.ActivitiesAndViews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import swarm.swarmcomposerapp.Model.Composition;
import swarm.swarmcomposerapp.R;

/**
 * Utils class to create and update the RecyclerView
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private List<Composition> compList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, author, date;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            author = (TextView) view.findViewById(R.id.author);
            date = (TextView) view.findViewById(R.id.date);
        }
    }


    public ListAdapter(List<Composition> compList) {
        this.compList = compList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Composition comp = compList.get(position);
        holder.name.setText(comp.getName());
        holder.author.setText(comp.getOwner().getFirstName() + " " + comp.getOwner().getLastName());
        //TODO Display icon based on whether Composition is Owned, Shared or Public
        holder.date.setText("Last Updated: "+
                DateFormat.getInstance().format((new Date(comp.getLastUpdate()*1000))));
    }

    @Override
    public int getItemCount() {
        return compList.size();
    }
}