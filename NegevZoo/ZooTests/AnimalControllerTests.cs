using NegevZoo.Controllers;
using Backend.Models;
using System.Linq;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace ZooTests
{
    [TestClass]
    public class AnimalControllerTests
    {
        private  AnimalController animalsController;

        [TestInitialize]
        public void SetUp()
        {
            // The line below must be in every setup of each test. otherwise it will not be in a testing environment.
            ControllerBase.isTesting = true;
            animalsController = new AnimalController();
        }

        [TestMethod]
        public void getAllAnimals()
        {
            Assert.AreEqual(3, animalsController.GetAllAnimals().Count());
        }

        [TestMethod]
        public void getAnimalById()
        {
            var animal = animalsController.GetAnimalById(1, 1);
            Assert.IsInstanceOfType(animal, typeof(Animal));
            Animal animalObject = (Animal)animal;

            Assert.AreEqual(1, animalObject.Id);
            Assert.AreEqual("בבון הזית", animalObject.Name);
        }

        [TestMethod]
        public void getAnimalByIdWrongId()
        {
            var animal = animalsController.GetAnimalById(-4, 1);

            Assert.AreEqual(null, animal);
        }
    }
}
