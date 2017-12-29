package dima.sabor.recipeDetails;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dima.sabor.NonScrollListView;
import dima.sabor.R;
import dima.sabor.base.BaseActivityImpl;
import dima.sabor.dependencyinjection.App;
import dima.sabor.dependencyinjection.activity.ActivityModule;
import dima.sabor.dependencyinjection.view.ViewModule;
import dima.sabor.model.Recipe;
import dima.sabor.recipesList.RecipesListActivityImpl;

public class RecipeDetailsActivityImpl extends BaseActivityImpl implements RecipeDetailsActivity {
    @BindView(R.id.recipe_detail_title)
    TextView title;

    @BindView(R.id.recipe_detail_ingredients)
    NonScrollListView ingredients;

    @BindView(R.id.recipe_detail_description)
    TextView description;

    @BindView(R.id.recipe_detail_time)
    TextView time;

    @BindView(R.id.recipe_detail_people)
    TextView people;

    @BindView(R.id.recipe_detail_difficulty)
    TextView difficulty;

    @BindView(R.id.recipe_detail_map)
    MapView place;

    @BindView(R.id.product_detail_viewpager)
    ViewPager viewPager;

    @BindView(R.id.product_detail_toolbar)
    Toolbar toolbar;

    @BindView(R.id.product_detail_dots_layout)
    LinearLayout dotsLayout;

    @Inject
    RecipeDetailsPresenter presenter;

    ImageViewPageAdapter viewPageAdapter;
    int numImages;
    ImageView[] dots;
    Recipe r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        ((App) getApplication())
                .getComponent()
                .plus(new ActivityModule(this),
                        new ViewModule(this))
                .inject(this);

        ButterKnife.bind(this);
        Gson gson = new Gson();
        r = gson.fromJson(getIntent().getStringExtra("recipe"),Recipe.class);
        onDetailsRetrieved(r);

        setUpBackActionBar(toolbar);


    }

    @Override
    public void onDetailsRetrieved(Recipe returnParam) {
        //Fake mentre no hi hagi imatges

        //List<String> fakeList = new ArrayList<>();
        //fakeList.add("https://photos6.spartoo.es/photos/231/231523/231523_350_A.jpg");

        /*addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryDialog();
            }
        });*/

        setUpViewPager(returnParam.getImages());
        //presenter.getImagesProduct(returnParam.getId());

        setUpProductDetails(returnParam);

        //setUpDotCounter();

        //presenter.getDesiredCategories(returnParam.getId());
    }

    private void setUpProductDetails(Recipe recipe) {
        title.setText(recipe.getTitle());
        description.setText(recipe.getDescription());
        difficulty.setText(recipe.getDifficulty());
        people.setText(recipe.getPeople());
        time.setText(recipe.getTime());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, recipe.getIngredients());

        ingredients.setAdapter(adapter);
        //TODO: Como poner el sitio situado en el mapa
        //place.set
    }

    public void setUpViewPager(List<String> images) {
        Log.i("Images", "images1: "+ images.get(0));
        List<Bitmap> imagesUrl = new ArrayList<>();
        for(String image: images) {
            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imagesUrl.add(decodedByte);
        }
        this.viewPageAdapter = new ImageViewPageAdapter(this, imagesUrl);
        viewPager.setAdapter(viewPageAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < numImages; i++) {
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.recipe_details_dot_not_selected));
                }

                dots[position].setImageDrawable(getResources().getDrawable(R.drawable.recipe_details_dot_selected));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setUpDotCounter();
    }

    private void setUpDotCounter() {
        numImages = viewPageAdapter.getCount();
        dots = new ImageView[numImages];

        for (int i = 0; i < numImages; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.recipe_details_dot_not_selected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            dotsLayout.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.recipe_details_dot_selected));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void goToShowRecipesList(){
        startActivity(new Intent(this, RecipesListActivityImpl.class));
        finish();
    }
}