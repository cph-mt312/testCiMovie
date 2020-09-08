package facades;

import entities.Movie;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class MovieFacade {

    private static MovieFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private MovieFacade() {
    }

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        MovieFacade facade = MovieFacade.getMovieFacade(emf);
        Movie m1 = facade.addMovie("Avatar", 2009, 7.8);
        Movie m2 = facade.addMovie("The Room", 2003, 3.7);
        Movie m3 = facade.addMovie("Harry Potter and the Prisoner of Azkaban", 2004, 7.9);
        Movie m4 = facade.addMovie("The Godfather", 1972, 9.2);
        Movie m5 = facade.addMovie("Alone in the Dark", 2005, 2.4);
        Movie m6 = facade.addMovie("Pulp Fiction", 1994, 8.9);
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static MovieFacade getMovieFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MovieFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<Movie> getAllMovies() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery query
                    = em.createQuery("SELECT m FROM Movie m", Movie.class);
            List<Movie> movies = query.getResultList();
            return movies;
        } finally {
            em.close();
        }
    }

    public Movie getMovieById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery query
                    = em.createQuery("SELECT m FROM Movie m WHERE m.id = :id", Movie.class);
            query.setParameter("id", id);
            Movie movie = (Movie) query.getSingleResult();
            return movie;
        } finally {
            em.close();
        }
    }

    public Movie getMovieByTitle(String title) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery query
                    = em.createQuery("SELECT m FROM Movie m WHERE m.title = :title", Movie.class);
            query.setParameter("title", title);
            Movie movie = (Movie) query.getSingleResult();
            return movie;
        } finally {
            em.close();
        }
    }

    public Movie addMovie(String title, int year, double rating) {
        EntityManager em = emf.createEntityManager();
        try {
            Movie movie = new Movie(title, year, rating);
            em.getTransaction().begin();
            em.persist(movie);
            em.getTransaction().commit();
            return movie;
        } finally {
            em.close();
        }
    }

    public List<Movie> getMoviesWithHighestRating() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery query
                    = em.createQuery("SELECT m FROM Movie m WHERE m.rating = (SELECT MAX(m.rating) FROM Movie m)", Movie.class);
            List<Movie> movies = query.getResultList();
            return movies;
        } finally {
            em.close();
        }
    }

    public long getMovieCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long movieCount = (long) em.createQuery("SELECT COUNT(m) FROM Movie m").getSingleResult();
            return movieCount;
        } finally {
            em.close();
        }

    }

}