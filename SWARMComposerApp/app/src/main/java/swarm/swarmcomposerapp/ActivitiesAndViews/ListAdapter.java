package swarm.swarmcomposerapp.ActivitiesAndViews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import swarm.swarmcomposerapp.Model.Composition;
import swarm.swarmcomposerapp.R;

/**
 * Utils class to create and update the RecyclerView
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private List<Composition> compList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, author;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            author = view.findViewById(R.id.author);
        }
    }

    /**
     * update the data set for this recyclerView
     * @param compList list of compositions
     */
    public void setCompList(List<Composition> compList){
        if(compList != null) {
            this.compList = compList;
            notifyDataSetChanged();
        }
    }


    public ListAdapter(List<Composition> compList) {
        if(compList == null){
            this.compList = new ArrayList<>();
        } else {
            this.compList = compList;
        }
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
        holder.author.setText(comp.getOwner().getFullName());
    }

    @Override
    public int getItemCount() {
        return compList.size();
    }
}