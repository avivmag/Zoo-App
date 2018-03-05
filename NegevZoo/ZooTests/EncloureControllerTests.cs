using NegevZoo.Controllers;
using Backend.Models;
using System.Linq;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Backend;
using System.Collections.Generic;

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

        #region GetAllEnclosures()
        [TestMethod]
        public void GetAllEnclosuresLangHe()
        {
            Assert.AreEqual(2, enclosureController.GetAllEnclosures((int)Languages.he).Count());
        }

        [TestMethod]
        public void getAllEnclosuresLangEng()
        {
            Assert.AreEqual(2, enclosureController.GetAllEnclosures((int)Languages.en).Count());
        }

        [TestMethod]
        public void getAllEnclosuresLangNotExist()
        {
            Assert.AreEqual(null, enclosureController.GetAllEnclosures(nonExistantLang));
        }
        #endregion

        #region GetEnclosureById
        [TestMethod]
        public void GetEnclosuresByValidId()
        {
            var enc = enclosureController.GetEnclosureById(2);

            //default in hebrew
            Assert.IsNotNull(enc);
            Assert.AreEqual(2, enc.Id);
            Assert.AreEqual("תצוגת הקופים", enc.Name);

            //english
            enc = enclosureController.GetEnclosureById(1,2);
            Assert.IsNotNull(enc);
            Assert.AreEqual(1, enc.Id);
            Assert.AreEqual("Monkeys", enc.Name);
        }

        [TestMethod]
        public void GetEnclosuresByIdWrongId()
        {
            var enc = enclosureController.GetEnclosureById(400);

            Assert.IsNull(enc);
        }

        [TestMethod]
        public void GetEnclosuresByIdWrongLanguage()
        {
            var enc = enclosureController.GetEnclosureById(1,nonExistantLang);

            Assert.IsNull(enc);
        }
        #endregion

        #region GetEnclosureByName
        [TestMethod]
        public void GetEnclosuresByNameValidInput()
        {
            IEnumerable<Enclosure> encs = enclosureController.GetEnclosureByName("תצוגת הקופים", 1);

            Assert.AreEqual(1, encs.Count());

            Enclosure enc = encs.ElementAt(0);

            //default in hebrew
            Assert.IsNotNull(enc);
            Assert.AreEqual(2, enc.Id);
            Assert.AreEqual("תצוגת הקופים", enc.Name);

            //english
            encs = enclosureController.GetEnclosureByName("Houman Monkeys", 2);

            Assert.AreEqual(1, encs.Count());

            enc = encs.ElementAt(0);

            Assert.IsNotNull(enc);
            Assert.AreEqual(3, enc.Id);
            Assert.AreEqual("Houman Monkeys", enc.Name);
        }

        [TestMethod]
        public void GetEnclosuresByNamePartialyName()
        {
            IEnumerable<Enclosure> encs = enclosureController.GetEnclosureByName("Monkeys", 2);

            Assert.AreEqual(2, encs.Count());
            Assert.AreEqual("Monkeys", encs.ElementAt(0).Name);
            Assert.AreEqual("Houman Monkeys", encs.ElementAt(1).Name);
        }

        [TestMethod]
        public void GetEnclosuresByNameWrongName()
        {
            var encs = enclosureController.GetEnclosureByName("שימפנזות");

            Assert.IsNotNull(encs);
            Assert.AreEqual(0, encs.Count());
        }

        [TestMethod]
        public void GetEnclosuresByNameWrongLanguage()
        {
            var enc = enclosureController.GetEnclosureByName("Houman Monkeys", nonExistantLang);

            Assert.IsNull(enc);
        }

        [TestMethod]
        public void GetEnclosuresByNameValidNameWrongLang()
        {
            var encs = enclosureController.GetEnclosureByName("Houman Monkeys");

            Assert.IsNotNull(encs);
            Assert.AreEqual(0, encs.Count());
        }
        #endregion

        //TODO: Add tests after implementaion.
        #region GetEnclsoureByPosition

        #endregion

        #region GetRecurringEvents
        [TestMethod]
        public void GetAllRecutnigEventsValidInput()
        {
            var encs = enclosureController.GetAllEnclosures((int)Languages.he);
            Assert.IsNotNull(encs);
            Assert.AreEqual(2, encs.Count());

            var enc = encs.ElementAt(0);
            Assert.IsNotNull(enc);
            Assert.AreEqual(2, enc.Id);
            Assert.AreEqual("תצוגת הקופים", enc.Name);

            var recurringEvents = enclosureController.GetRecurringEvents(enc.Id, (int)Languages.he);
            Assert.IsNotNull(recurringEvents);
            Assert.AreEqual(1, recurringEvents.Count());

            var recEvent = recurringEvents.ElementAt(0);
            Assert.IsNotNull(recEvent);
            Assert.AreEqual(2, recEvent.Id);
            Assert.AreEqual(enc.Id, recEvent.Id);
            Assert.AreEqual(enc.Language, recEvent.Language);
            Assert.AreEqual(10.30, recEvent.StartHour);
        }

        [TestMethod]
        public void GetRecurringEventValidInputEmptyList()
        {
            var encs = enclosureController.GetAllEnclosures((int)Languages.he);
            Assert.IsNotNull(encs);
            Assert.AreEqual(2, encs.Count());

            var enc = encs.ElementAt(1);
            Assert.IsNotNull(enc);
            Assert.AreEqual(4, enc.Id);
            Assert.AreEqual("קופי אדם", enc.Name);

            var recurringEvents = enclosureController.GetRecurringEvents(enc.Id, (int)Languages.he);
            Assert.IsNotNull(recurringEvents);
            Assert.AreEqual(0, recurringEvents.Count());
        }

        [TestMethod]
        public void GetRecurringEventsValidIdWrongLanguage()
        {
            var recEvents = enclosureController.GetRecurringEvents(1);

            Assert.IsNotNull(recEvents);
            Assert.AreEqual(0, recEvents.Count());
        }

        [TestMethod]
        public void GetRecurringEventsWrongId()
        {
            var recEvents = enclosureController.GetRecurringEvents(200);

            Assert.IsNull(recEvents);
        }

        [TestMethod]
        public void GetRecurringEventsWrongLanguage()
        {
            var recEvents = enclosureController.GetRecurringEvents(nonExistantLang);

            Assert.IsNull(recEvents);
        }
        #endregion


        #region UpdateEnclosure
        [TestMethod]
        public void UpdateEnclosureValidTest()
        {
            var encs = enclosureController.GetAllEnclosures((int)Languages.en);
            Assert.IsNotNull(encs);
            Assert.AreEqual(2, encs.Count());

            var monkeyEncEn = encs.ElementAt(0);
            Assert.AreEqual("Monkeys", monkeyEncEn.Name);

            monkeyEncEn.Name = "kaki";
            enclosureController.UpdateEnclosure(monkeyEncEn);

        }
        #endregion
    }
}
