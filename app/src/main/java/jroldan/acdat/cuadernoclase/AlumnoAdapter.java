package jroldan.acdat.cuadernoclase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AlumnoAdapter extends RecyclerView.Adapter<AlumnoAdapter.AlumnoHolder> {
    private List<Alumno> listaAlumnos;
    private LayoutInflater inflater;

    public AlumnoAdapter(Context context) {
        this.listaAlumnos = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public AlumnoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.alumno_item, parent, false);
        return new AlumnoHolder(view);
    }

    @Override
    public void onBindViewHolder(AlumnoHolder holder, int position) {
        Alumno alumno = listaAlumnos.get(position);
        holder.nombre.setText(alumno.toString());
        holder.direccion.setText(alumno.getDireccion());
    }

    @Override
    public int getItemCount() {
        return listaAlumnos.size();
    }

    public void set(ArrayList<Alumno> lista) {
        this.listaAlumnos = lista;
        notifyItemRangeChanged(0, lista.size() - 1);
    }

    public Alumno getAt(int position) {
        return this.listaAlumnos.get(position);
    }

    public void add(Alumno alumno) {
        this.listaAlumnos.add(alumno);
        notifyItemChanged(listaAlumnos.size() - 1);
        notifyItemRangeChanged(0, listaAlumnos.size() - 1);
    }

    public void modifyAt(Alumno alumno, int position) {
        this.listaAlumnos.set(position, alumno);
        notifyItemChanged(position);
    }

    public void removeAt(int position) {
        this.listaAlumnos.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, listaAlumnos.size() - 1);
    }

    public class AlumnoHolder extends RecyclerView.ViewHolder {
        private TextView nombre, direccion;

        public AlumnoHolder(View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.txvItemAlumnoNombre);
            direccion = (TextView) itemView.findViewById(R.id.txvItemAlumnoDireccion);
        }
    }
}