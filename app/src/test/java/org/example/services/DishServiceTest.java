package org.example.services;

import org.example.models.DishModel;
import org.example.repositories.DishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DishServiceTest {

  private DishService dishService;
  private DishRepository mockRepository;

  @BeforeEach
  void setup() throws Exception {
    mockRepository = mock(DishRepository.class);
    setMockInstance(mockRepository);
    dishService = new DishService();
  }

  private void setMockInstance(DishRepository mockRepository) throws Exception {
    Field instanceField = DishRepository.class.getDeclaredField("instance");
    instanceField.setAccessible(true);
    instanceField.set(null, mockRepository);
  }

  @Test
  @DisplayName("Test Create Dish")
  void testCreateDish() {
    dishService.createDish("Plato 1", "Descripción del plato", 10.0);

    ArgumentCaptor<DishModel> dishCaptor = ArgumentCaptor.forClass(DishModel.class);
    verify(mockRepository).addDish(dishCaptor.capture());

    DishModel capturedDish = dishCaptor.getValue();
    assertEquals("Plato 1", capturedDish.getName());
    assertEquals("Descripción del plato", capturedDish.getDescription());
    assertEquals(10.0, capturedDish.getPrice());
  }

  @Test
  @DisplayName("Test Get Average Rating of Dish")
  void testGetAverageRatingOfDish() {
    DishModel mockDish = mock(DishModel.class);
    when(mockRepository.getDish("Plato 1")).thenReturn(mockDish);
    when(mockDish.getAverageRating()).thenReturn(4.5);

    double averageRating = dishService.getAverageRatingOfDish("Plato 1");

    assertEquals(4.5, averageRating);
  }

  @Test
  @DisplayName("Test Get Average Rating of Dish - Dish Not Found")
  void testGetAverageRatingOfDishNotFound() {
    when(mockRepository.getDish("Plato 1")).thenReturn(null);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      dishService.getAverageRatingOfDish("Plato 1");
    });

    assertEquals("Plato no encontrado: Plato 1", exception.getMessage());
  }

  @Test
  @DisplayName("Test Update Method")
  void testUpdate() {
    dishService.update("Nuevo plato añadido");
  }
  @Test
  @DisplayName("Test Create Dish With Null Name")
  void testCreateDishWithNullName() {
    assertThrows(IllegalArgumentException.class, () -> dishService.createDish(null, "Descripción del plato", 10.0));
  }

  @Test
  @DisplayName("Test Create Dish With Null Description")
  void testCreateDishWithNullDescription() {
    assertThrows(IllegalArgumentException.class, () -> dishService.createDish("Plato 1", null, 10.0));
  }

  @Test
  @DisplayName("Test Create Dish With Negative Price")
  void testCreateDishWithNegativePrice() {
    assertThrows(IllegalArgumentException.class, () -> dishService.createDish("Plato 1", "Descripción del plato", -10.0));
  }

  @Test
  @DisplayName("Test Update Method With Non-Dish Message")
  void testUpdateWithNonDishMessage() {
    dishService.update("Some other message");
  }
}