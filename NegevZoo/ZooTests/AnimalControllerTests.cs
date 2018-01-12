using NegevZoo.Controllers;
using Backend;
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
            //Assert.AreEqual(3, animalsController.GetAllAnimals(1).Count());
        }
    }
}
