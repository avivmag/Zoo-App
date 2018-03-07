using NegevZoo.Controllers;
using Backend.Models;
using System.Linq;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Backend;

namespace ZooTests
{
    [TestClass]
    public class AnimalControllerTests
    {
        private  AnimalController animalsController;
        private int nonExistantLang;
        [TestInitialize]
        public void SetUp()
        {
            // The line below must be in every setup of each test. otherwise it will not be in a testing environment.
            ControllerBase.isTesting = true;
            animalsController = new AnimalController();
            nonExistantLang = 100;
        }

        [TestMethod]
        public void getAllAnimalsLangHe()
        {
            Assert.AreEqual(3, animalsController.GetAllAnimals((int)Languages.he).Count());
        }

        [TestMethod]
        public void getAllAnimalsLanguageNotExist()
        {
            Assert.AreEqual(0, animalsController.GetAllAnimals(nonExistantLang).Count());
        }

        [TestMethod]
        public void getAnimalById()
        {
            var animal = animalsController.GetAnimalById(1, (int)Languages.en);
            Assert.IsInstanceOfType(animal, typeof(Animal));
            Animal animalObject = (Animal)animal;

            Assert.AreEqual(1, animalObject.Id);
            Assert.AreEqual("Olive Baboon", animalObject.Name);
        }

        [TestMethod]
        public void getAnimalByIdWrongId()
        {
            var animal = animalsController.GetAnimalById(-4, (int)Languages.he);

            Assert.AreEqual(null, animal);
        }

        [TestMethod]
        public void getAnimalByIdLanguageNotExist()
        {
            var animal = animalsController.GetAnimalById(4, nonExistantLang);

            Assert.AreEqual(null, animal);
        }
    }
}
