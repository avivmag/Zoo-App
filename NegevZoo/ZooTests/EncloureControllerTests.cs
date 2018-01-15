using NegevZoo.Controllers;
using Backend.Models;
using System.Linq;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Backend;

namespace ZooTests
{
    [TestClass]
    public class EncloureControllerTests
    {
        private EnclosureController enclosureController;
        private int nonExistantLang;

        [TestInitialize]
        public void SetUp()
        {
            // The line below must be in every setup of each test. otherwise it will not be in a testing environment.
            ControllerBase.isTesting    = true;
            enclosureController         = new EnclosureController();
            nonExistantLang             = 100;
        }

        [TestMethod]
        public void getAllEnclosuresLangHe()
        {
            Assert.AreEqual(1, enclosureController.GetAllEnclosures((int)Languages.he).Count());
        }

        [TestMethod]
        public void getAllEnclosuresLangEng()
        {
            Assert.AreEqual(1, enclosureController.GetAllEnclosures((int)Languages.en).Count());
        }

        [TestMethod]
        public void getAllEnclosuresLangNotExist()
        {
            Assert.AreEqual(0, enclosureController.GetAllEnclosures(nonExistantLang).Count());
        }
    }
}
