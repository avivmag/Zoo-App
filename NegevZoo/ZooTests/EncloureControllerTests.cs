using NegevZoo.Controllers;
using Backend.Models;
using System.Linq;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace ZooTests
{
    [TestClass]
    public class EncloureControllerTests
    {
        private EnclosureController enclosureController;

        [TestInitialize]
        public void SetUp()
        {
            // The line below must be in every setup of each test. otherwise it will not be in a testing environment.
            ControllerBase.isTesting = true;
            enclosureController = new EnclosureController();
        }

        [TestMethod]
        public void getAllEnclosures()
        {
            Assert.AreEqual(1, enclosureController.GetAllEnclosures().Count());
        }
    }
}
