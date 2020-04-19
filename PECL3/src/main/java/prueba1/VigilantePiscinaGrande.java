package prueba1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class VigilantePiscinaGrande extends Vigilante {

    BlockingQueue<Visitante> zonaActividad;

    public VigilantePiscinaGrande(String id, ArrayBlockingQueue<Visitante> colaEspera, ArrayBlockingQueue<Visitante> zonaActividad) {
        super(id, colaEspera);
        this.zonaActividad = zonaActividad;
    }

    public long getTiempoVigilancia() {
        return 500;
    }

    public long getTiempoExpulsion() {
        return (long) ((int) (500) + (500 * Math.random()));
    }

    public Permiso tipoPermiso(Visitante visitante) {
        Permiso tipoPermiso = Permiso.PERMITIDO;
        if (visitante instanceof Ninio) {
            tipoPermiso = Permiso.CON_ACOMPANIANTE;
        }
        return tipoPermiso;
    }

    private Visitante randomVisitante() {
        Visitante visitante = null;
        int n = (int) (getZonaActividad().size() * Math.random());
        Iterator<Visitante> iterator = getZonaActividad().iterator();
        List<Visitante> visitantes = new ArrayList<>();
        iterator.forEachRemaining(visitantes::add);
        visitante = visitantes.get(n);
        if (visitante instanceof Acompaniante) {
            visitante = visitantes.get(n - 1);
        }
        return visitante;
    }

    public void run() {
        Visitante visitanteParaExpulsar;
        while (true) {
            try {
                for (Visitante visitante : colaEspera) { 
                    sleep(getTiempoVigilancia());
                    Permiso permiso = tipoPermiso(visitante);
                    visitante.setPermisoActividad(permiso);
                    if (getZonaActividad().remainingCapacity() == 0) {
                        visitanteParaExpulsar = randomVisitante();
                        visitanteParaExpulsar.interrupt();
                        sleep(getTiempoExpulsion());
                        System.out.println("Vigilante " + getIdentificador() + " echando al visitante " + visitanteParaExpulsar.getIdentificador() + " con edad " + visitanteParaExpulsar.getEdad());
                        System.out.println("----------------------------------------------------------------------------------------------------------");
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public BlockingQueue<Visitante> getZonaActividad() {
        return zonaActividad;
    }

    public void setZonaActividad(BlockingQueue<Visitante> zonaActividad) {
        this.zonaActividad = zonaActividad;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public BlockingQueue<Visitante> getEspacio() {
        return colaEspera;
    }

    public void setEspacio(BlockingQueue<Visitante> espacio) {
        this.colaEspera = espacio;
    }

}
