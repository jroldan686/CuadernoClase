package jroldan.acdat.cuadernoclase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FaltaAdapter extends RecyclerView.Adapter<FaltaAdapter.FaltaHolder> {
    private List<Falta> faltas;
    private LayoutInflater inflater;

    public FaltaAdapter(Context context) {
        this.faltas = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public FaltaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewRoot = inflater.inflate(R.layout.falta_item, parent, false);
        return new FaltaHolder(viewRoot);
    }

    @Override
    public void onBindViewHolder(FaltaHolder holder, int position) {
        Falta falta = faltas.get(position);
        holder.txvFalta.setText(falta.getAlumno());
        holder.txvFecha.setText(falta.getFecha());
    }

    @Override
    public int getItemCount() {
        return this.faltas.size();
    }

    public void set(ArrayList<Falta> faltas) {
        this.faltas = faltas;
        notifyItemRangeChanged(0, faltas.size() - 1);
    }

    public Falta getAt(int position) {
        return this.faltas.get(position);
    }

    public void add(Falta falta) {
        this.faltas.add(falta);
        notifyItemChanged(faltas.size() - 1);
        notifyItemRangeChanged(0, faltas.size() - 1);
    }

    public void modifyAt(Falta falta, int position) {
        this.faltas.set(position, falta);
        notifyItemChanged(position);
    }

    public void removeAt(int position) {
        this.faltas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, faltas.size() - 1);
    }

    public class FaltaHolder extends RecyclerView.ViewHolder {
        private TextView txvFalta, txvFecha;

        public FaltaHolder(View itemView) {
            super(itemView);
            txvFalta = (TextView) itemView.findViewById(R.id.txvItemFaltaFalta);
            txvFecha = (TextView) itemView.findViewById(R.id.txvItemFaltaFecha);
        }
    }
}
