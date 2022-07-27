package com.ata.lambdaexpressions.classroom;

import com.ata.lambdaexpressions.classroom.converter.RecipeConverter;
import com.ata.lambdaexpressions.classroom.dao.CartonDao;
import com.ata.lambdaexpressions.classroom.dao.RecipeDao;
import com.ata.lambdaexpressions.classroom.exception.CartonCreationFailedException;
import com.ata.lambdaexpressions.classroom.exception.RecipeNotFoundException;
import com.ata.lambdaexpressions.classroom.model.Carton;
import com.ata.lambdaexpressions.classroom.model.Ingredient;
import com.ata.lambdaexpressions.classroom.model.Recipe;
import com.ata.lambdaexpressions.classroom.model.Sundae;

import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;

/**
 * Provides Ice Cream Parlor functionality.
 */
public class IceCreamParlorService {
    private final RecipeDao     recipeDao;
    private final CartonDao     cartonDao;
    private final IceCreamMaker iceCreamMaker;

    /**
     * Constructs service with the provided DAOs.
     * @param recipeDao the RecipeDao to use for accessing recipes
     * @param cartonDao the CartonDao to use for accessing ice cream cartons
     */
    @Inject
    public IceCreamParlorService(RecipeDao recipeDao, CartonDao cartonDao, IceCreamMaker iceCreamMaker) {
        this.recipeDao = recipeDao;
        this.cartonDao = cartonDao;
        this.iceCreamMaker = iceCreamMaker;
    }

    /**
     * Creates and returns the sundae defined by the given ice cream flavors.
     * If a flavor is not found or we have none of that flavor left, the sundae
     * is returned, but without that flavor. (We'll only charge the customer for
     * the scoops they are returned)
     * @param flavorNames List of flavors to include in the sundae
     * @return The newly created Sundae
     */
    public Sundae getSundae(List<String> flavorNames) {
        // This does the filtering out of any unknown flavors, so only
        // Cartons of known flavors will be returned.
        List<Carton> cartons = cartonDao.getCartonsByFlavorNames(flavorNames);

        // PHASE 1: Use removeIf() to remove any empty cartons from cartons
        // cartons.removeIf((aCarton) -> aCarton.isEmpty()); // call isEmpty() for each carton
        // cartons.removeIf( aCarton  -> aCarton.isEmpty()); // call isEmpty() for each carton
           cartons.removeIf(Carton::isEmpty); // call isEmpty() for each carton

        return buildSundae(cartons);
    }

    @VisibleForTesting
    Sundae buildSundae(List<Carton> cartons) {
        Sundae sundae = new Sundae();

        // PHASE 2: Use forEach() to add one scoop of each flavor
        // remaining in cartons
        //
        // Call the Sundae.addScoop() for each Carton left after teh removeIf()
        //
        // Go through the list of Cartons and get a scoop from each one for the sundae
        // Since cartons is a List we can use a forEach() to go through the List

        // We used the Iterable forEach instead of the Stream interface forEach
        cartons.forEach((raymond) -> sundae.addScoop(raymond.getFlavor()));

        return sundae;
    }

    /**
     * Prepares the specified flavors, creating 1 carton of each provided
     * flavor.
     *
     * A flavor name that doesn't correspond
     * to a known recipe will result in CartonCreationFailedException, and
     * no Cartons will be created.
     *
     * @param flavorNames List of names of flavors to create new batches of
     * @return the number of cartons produced by the ice cream maker
     */
    public int prepareFlavors(List<String> flavorNames) {

        // Use the map() helper method we wrote to get the recipes
        List<Recipe> recipes = map(  // this map() IS NOT THE STREAM map() METHOD - it is a helper method in this class
            flavorNames,       // this is called input in the helper method
            (flavorName) -> {  // this is called converter the helper method
                // trap the checked exception, RecipeNotFoundException, and
                // wrap in a runtime exception because our lambda can't throw
                // checked exceptions
                try {
                    return recipeDao.getRecipe(flavorName);
                } catch (RecipeNotFoundException e) {
                    throw new CartonCreationFailedException("Could not find recipe for " + flavorName, e);
                }
            }
        );

        // PHASE 3: Replace right hand side: use map() to convert List<Recipe> to List<Queue<Ingredient>>
        // List<Queue<Ingredient>> ingredientQueues = new ArrayList<>(); //  Replaced by a .map() to create the new List

        List<Queue<Ingredient>> ingredientQueues = recipes.stream().map((aRecipe) -> RecipeConverter.fromRecipeToIngredientQueue(aRecipe))
                                                                   .collect(Collectors.toList());

        return makeIceCreamCartons(ingredientQueues);
    }

    @VisibleForTesting
    int makeIceCreamCartons(List<Queue<Ingredient>> ingredientQueues) {
        // don't change any of the lines that touch cartonsCreated.
        int cartonsCreated = 0;
        for (Queue<Ingredient> ingredients : ingredientQueues) {

            // PHASE 4: provide Supplier to prepareIceCream()
            if (iceCreamMaker.prepareIceCreamCarton(() -> ingredients.poll())) {
                cartonsCreated++;
            }
        }

        return cartonsCreated;
    }

    /**
     * Converts input list of type T to a List of type R, where each entry in the return
     * value is the output of converter applied to each entry in input.
     *
     * (We will get to Java streams in a later lesson, at which point we won't need a helper method
     * like this.)
     */
    // this method receives two parameters:
    //
    //  1. input - which is a List of a generic type
    //  2. converter - which is a method that receives two parameters
    //                 the method passed can be a named method or a Lambda expression
    //
    // private <T, R> this method will receive different data types,
    //         whatever the 1st is we'll call 'T' and 2nd we'll call 'R'
    // List<R> - the List will be of the type of the second parameter we called 'R'
    // List<T> - the List will be of the type of the second parameter we called 'T'
    // Function<T, R> - the method passed as a parameter will receive a parm of type 'T' and type 'R'
    private <T, R> List<R> map(List<T> input, Function<T, R> converter) {
        return input.stream() // Use the stream() method on the list passed as the 1st parameter
            .map(converter)   // calling the stream.map() method with the function we were passed
            .collect(Collectors.toList());
    }
}
