package com.example.fantapronosticiesame.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fantapronosticiesame.Interface.OnItemClickListener;
import com.example.fantapronosticiesame.Model.Cookie;
import com.example.fantapronosticiesame.Model.Lega;
import com.example.fantapronosticiesame.Model.User;
import com.example.fantapronosticiesame.R;

import java.util.List;

public class LegaAdapter extends RecyclerView.Adapter<LegaAdapter.LegaViewHolder>{
    private OnItemClickListener onItemClickListener;
    private static List<Lega> leghe;
    public LegaAdapter (List<Lega> listaLeghe, OnItemClickListener listener) {
        this.leghe = listaLeghe;
        this.onItemClickListener = listener;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    @NonNull
    @Override
    public LegaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lega_layout, parent, false);
        return new LegaViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LegaViewHolder holder, int position) {
        holder.bind(leghe.get(position));
    }

    @Override
    public int getItemCount() {
        return leghe.size();
    }


    static class LegaViewHolder extends RecyclerView.ViewHolder {
        private final TextView nomeLega;
        private final TextView partecipanti;
        private final TextView textView;
        private final OnItemClickListener listener;

        public LegaViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            nomeLega = itemView.findViewById(R.id.titolo);
            partecipanti = itemView.findViewById(R.id.partecipanti);
            textView = itemView.findViewById(R.id.frase);
            this.listener = listener;
            itemView.setOnClickListener(v -> {
                listener.onItem(textView.getText().toString(), leghe.get(getAdapterPosition()));
            });
        }

        public void bind(Lega lega) {
            nomeLega.setText(lega.getNomeLega());
            String numPersone = lega.numPartecipanti() + "/" + lega.getMaxPartecipanti();
            partecipanti.setText(numPersone);
            int cont = 0;
            for (User user: lega.getListaPartecipanti()) {
                if (user.getId().equals(Cookie.getCookieId())) {
                    textView.setText("Entra");
                    cont = 1;
                }
            }
            if (cont == 0) {
                textView.setText("Unisciti");
            }
        }
    }
}
