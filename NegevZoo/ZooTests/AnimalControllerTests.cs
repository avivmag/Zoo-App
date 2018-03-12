using NegevZoo.Controllers;
using Backend.Models;
using System.Linq;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Backend;
using System.Web.Http;

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
            nonExistantLang = 10000;
        }

        #region GetAllAnimals
        [TestMethod]
        public void GetAllAnimalsLangHe()
        {
            Assert.AreEqual(3, animalsController.GetAllAnimals((int)Languages.he).Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAllAnimalsLanguageNotExist()
        {
            animalsController.GetAllAnimals(nonExistantLang);
        }

        #endregion

        #region GetAnimalById
        [TestMethod]
        public void GetAnimalById()
        {
            var animal = animalsController.GetAnimalById(1, (int)Languages.en);
            Assert.IsInstanceOfType(animal, typeof(Animal));
            Animal animalObject = (Animal)animal;

            Assert.AreEqual(1, animalObject.Id);
            Assert.AreEqual("Olive Baboon", animalObject.Name);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAnimalByIdWrongId()
        {
            animalsController.GetAnimalById(-4, (int)Languages.he);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAnimalByIdLanguageNotExist()
        {
            animalsController.GetAnimalById(4, nonExistantLang);
        }

        #endregion
    }
}
