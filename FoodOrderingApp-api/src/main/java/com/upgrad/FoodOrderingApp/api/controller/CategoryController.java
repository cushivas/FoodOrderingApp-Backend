package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.model.CategoriesListResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/*
*  Category related end-points goes here..
* */

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

   /*
   *  Get all categories api, this returns array of objects
   *  containing id and food category
   *
   * */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET,path = "",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoriesListResponse> getAllCategories(){

        // Get all categories
        List<CategoryEntity> categoryEntities = categoryService.getAllCategoriesOrderedByName();
        if(!categoryEntities.isEmpty()) {
            List<CategoryListResponse> categoryListResponses = new LinkedList<>();
            categoryEntities.forEach(categoryEntity -> {
                CategoryListResponse categoryListResponse = new CategoryListResponse()
                        .id(UUID.fromString(categoryEntity.getUuid()))
                        .categoryName(categoryEntity.getCategoryName());
                categoryListResponses.add(categoryListResponse);
            });

            //Generate Response
            CategoriesListResponse categoriesListResponse = new CategoriesListResponse().categories(categoryListResponses);
            return new ResponseEntity<CategoriesListResponse>(categoriesListResponse, HttpStatus.OK);
        }else{
            return new ResponseEntity<CategoriesListResponse>(new CategoriesListResponse(),HttpStatus.OK);
        }
    }

    /*
    *  Get food items inside food category by Category id..
    *
    * */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET,path = "/{category_id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoryDetailsResponse> getCategoryById(@PathVariable(value = "category_id")final String categoryUuid) throws CategoryNotFoundException {

        if(categoryUuid == null || categoryUuid == "" || categoryUuid.length() == 0) {
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }
        CategoryEntity categoryEntity = categoryService.getCategoryById(categoryUuid);
        List<ItemEntity> itemEntities = categoryEntity.getItems();

        /*
            If
        */


        List<ItemList> itemLists = new LinkedList<>();
        itemEntities.forEach(itemEntity -> {
            ItemList itemList = new ItemList()
                    .id(UUID.fromString(itemEntity.getUuid()))
                    .price(itemEntity.getPrice())
                    .itemName(itemEntity.getitemName())
                    .itemType(ItemList.ItemTypeEnum.fromValue(itemEntity.getType().getValue()));
            itemLists.add(itemList);
        });

        //Creating CategoryDetailsResponse by adding the itemList and other details.
        CategoryDetailsResponse categoryDetailsResponse = new CategoryDetailsResponse()
                .categoryName(categoryEntity.getCategoryName())
                .id(UUID.fromString(categoryEntity.getUuid()))
                .itemList(itemLists);
        return new ResponseEntity<CategoryDetailsResponse>(categoryDetailsResponse,HttpStatus.OK);
    }

}
