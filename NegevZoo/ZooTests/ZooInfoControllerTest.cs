using Microsoft.VisualStudio.TestTools.UnitTesting;
using NegevZoo.Controllers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ZooTests
{
    [TestClass]
    public class ZooInfoControllerTest
    {
        private ZooInfoController ZooInfoController;

        [TestInitialize]
        public void SetUp()
        {
            // The line below must be in every setup of each test. otherwise it will not be in a testing environment.
            ControllerBase.isTesting = true;
            ZooInfoController = new ZooInfoController();
        }

        [TestMethod]
        public void getAllWallFeed()
        {
            Assert.AreEqual(4, ZooInfoController.getWallFeed().Count());
        }

        [TestMethod]
        public void getAllWallFeedLangHeb()
        {
            Assert.AreEqual(4, ZooInfoController.getWallFeed(2).Count());
        }
    }
}
