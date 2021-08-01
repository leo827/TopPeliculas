package com.moviesapptmdbapi.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.moviesapptmdbapi.Adapter.MoviesAdapter;
import com.moviesapptmdbapi.Model.Genre;
import com.moviesapptmdbapi.Model.Movie;
import com.moviesapptmdbapi.R;
import com.moviesapptmdbapi.Repository.MoviesRepository;
import com.moviesapptmdbapi.Reset.OnGetGenresCallback;
import com.moviesapptmdbapi.Reset.OnGetMoviesCallback;
import com.moviesapptmdbapi.Reset.OnMoviesClickCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    OnMoviesClickCallback clickCallback = new OnMoviesClickCallback() {
        @Override
        public void onClick(Movie movie) {
            Intent intent = new Intent(MainActivity.this, MovieActivity.class);
            intent.putExtra(MovieActivity.MOVIE_ID, movie.getId());
            startActivity(intent);
        }
    };
    //Ordenar por
    private String sortBY = MoviesRepository.POPULAR;
    //TODO: Elements Main Activity
    private RecyclerView rcView;
    private MoviesAdapter mAdapter;
    //    Este modelo / MoviesRepository.java
    private MoviesRepository mRepository;
    /*
     * Primera lista de modelo /Genre.java,
     * segundo booleano TRUE/FALSE isFetchingMovies,
     * Tercera int página actual = 1 =>incrementará...
     * */
    private List<Genre> movieGenres;
    private boolean isFetchingMovies;
    //    Método onCreate
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRepository = MoviesRepository.getInstance();
        rcView = (RecyclerView) findViewById(R.id.movies_list);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setupOneScrollListener() hace que RecyclerView muestre datos por orden
        setupOneScrollListener();
        //getGenres() METHOD es para mostrar Géneros de películas
        getGenres();
    }

    //Ordenar menú (Popular, Tasa superior, Próxima)
    //Cuando seleccione Ordenar, el icono mostrará el MENÚ de "res/menu/menu_movies_sort"
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort:
                showSortMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //agregar menu_movies_sort en Ordenar
    private void showSortMenu() {
        PopupMenu sortMenu = new PopupMenu(this, findViewById(R.id.sort));
        sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                currentPage = 1;
                switch (item.getItemId()) {
                    case R.id.popular:
                        sortBY = MoviesRepository.POPULAR;
                        getMovies(currentPage);
                        return true;
                    case R.id.top_rate:
                        sortBY = MoviesRepository.TOP_RATE;
                        getMovies(currentPage);
                        return true;
                    case R.id.upcoming:
                        sortBY = MoviesRepository.UPCOMING;
                        getMovies(currentPage);
                        return true;
                    default:
                        return false;
                }
            }
        });
        sortMenu.inflate(R.menu.menu_movies_sort);
        sortMenu.show();
    }

    //Hacer que el menú CLASIFICAR se muestre en MainActivity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movies, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupOneScrollListener() {
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        rcView.setLayoutManager(manager);
        rcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();
                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingMovies) {
                        getMovies(currentPage + 1);
                    }
                }
            }
        });
    }

    //MainActivity.getGenres de películas
    private void getGenres() {
        mRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                movieGenres = genres;
                getMovies(currentPage);
            }
            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void getMovies(int page) {
        isFetchingMovies = true;
        mRepository.getMovies(page, sortBY, new OnGetMoviesCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {
                Log.d("MoviesRepository", "Current Page = " + page);
                if (mAdapter == null) {
                    mAdapter = new MoviesAdapter(movies, movieGenres, clickCallback);
                    rcView.setAdapter(mAdapter);
                } else {
                    if (page == 1) {
                        mAdapter.clearMovies();
                    }
                    mAdapter.appendMovies(movies);
                }
                currentPage = page;
                isFetchingMovies = false;
                setTitle();
            }
            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void setTitle() {
        switch (sortBY) {
            case MoviesRepository.POPULAR:
                setTitle(getString(R.string.popular));
                break;
            case MoviesRepository.TOP_RATE:
                setTitle(getString(R.string.top_rate));
                break;
            case MoviesRepository.UPCOMING:
                setTitle(getString(R.string.upcoming));
                break;
        }
    }

    private void showError() {
        Toast.makeText(MainActivity.this, ".", Toast.LENGTH_LONG).show();
    }

}