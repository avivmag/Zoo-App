using NegevZoo.Controllers;
using DAL;
using System.Linq;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Collections.Generic;
using System.Web.Http;
using BL;
using System;
using DAL.Models;

namespace ZooTests
{
    [TestClass]
    public class EncloureControllerTests
    {
        private EnclosureController enclosureController;
        private int nonExistantLang;

        #region tests SetUp and TearDown
        [TestInitialize]
        public void SetUp()
        {
            // The line below must be in every setup of each test. otherwise it will not be in a testing environment.
            ControllerBase.isTesting = true;
            enclosureController = new EnclosureController();
            nonExistantLang = 100;
        }

        [TestCleanup]
        public void EnclosureCleanUp()
        {
            DummyDB.CleanDb();
        }

        #endregion

        #region GetAllEnclosureResults()
        [TestMethod]
        public void GetAllEnclosuresLangHe()
        {
            Assert.AreEqual(3, enclosureController.GetAllEnclosureResults((int)Languages.he).Count());
        }

        [TestMethod]
        public void GetAllEnclosuresLangEng()
        {
            Assert.AreEqual(3, enclosureController.GetAllEnclosureResults((int)Languages.en).Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAllEnclosuresLangNotExist()
        {
            enclosureController.GetAllEnclosureResults(nonExistantLang);
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
            Assert.AreEqual("קופי אדם", enc.Name);

            //english
            enc = enclosureController.GetEnclosureById(1, 2);
            Assert.IsNotNull(enc);
            Assert.AreEqual(1, enc.Id);
            Assert.AreEqual("Monkeys", enc.Name);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetEnclosuresByIdWrongId()
        {
            enclosureController.GetEnclosureById(400);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetEnclosuresByIdWrongLanguage()
        {
            enclosureController.GetEnclosureById(1, nonExistantLang);
        }
        #endregion

        #region GetEnclosureByName
        [TestMethod]
        public void GetEnclosuresByNameValidInput()
        {
            IEnumerable<EnclosureResult> encs = enclosureController.GetEnclosureByName("תצוגת הקופים", 1);

            Assert.AreEqual(1, encs.Count());

            EnclosureResult enc = encs.First();

            //default in hebrew
            Assert.IsNotNull(enc);
            Assert.AreEqual(1, enc.Id);
            Assert.AreEqual("תצוגת הקופים", enc.Name);

            //english
            encs = enclosureController.GetEnclosureByName("Houman Monkeys", 2);

            Assert.AreEqual(1, encs.Count());

            enc = encs.ElementAt(0);

            Assert.IsNotNull(enc);
            Assert.AreEqual(2, enc.Id);
            Assert.AreEqual("Houman Monkeys", enc.Name);
        }

        [TestMethod]
        public void GetEnclosuresByNamePartialyName()
        {
            IEnumerable<EnclosureResult> encs = enclosureController.GetEnclosureByName("Monkeys", 2);

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
        [ExpectedException(typeof(HttpResponseException))]
        public void GetEnclosuresByNameWrongLanguage()
        {
            enclosureController.GetEnclosureByName("Houman Monkeys", nonExistantLang);
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

        #region GetAllRecurringEvents
        [TestMethod]
        public void GetAllRecutnigEventsValidInput()
        {
            var encs = enclosureController.GetAllEnclosureResults((int)Languages.he);
            Assert.IsNotNull(encs);
            Assert.AreEqual(3, encs.Count());

            var enc = encs.ElementAt(0);
            Assert.IsNotNull(enc);
            Assert.AreEqual(1, enc.Id);
            Assert.AreEqual("תצוגת הקופים", enc.Name);

            var recurringEvents = enclosureController.GetRecurringEvents((int)enc.Id, (int)Languages.he);
            Assert.IsNotNull(recurringEvents);
            Assert.AreEqual(1, recurringEvents.Count());

            var recEvent = recurringEvents.ElementAt(0);
            Assert.IsNotNull(recEvent);
            Assert.AreEqual(1, recEvent.id);
            Assert.AreEqual(enc.Id, recEvent.enclosureId);
            Assert.AreEqual(enc.Language, recEvent.language);
            Assert.AreEqual(new TimeSpan(10, 30, 0), recEvent.startTime);
            Assert.AreEqual(new TimeSpan(11, 0, 0), recEvent.endTime);
        }

        [TestMethod]
        public void GetRecurringEventValidInputEmptyList()
        {
            var encs = enclosureController.GetAllEnclosureResults((int)Languages.he);
            Assert.IsNotNull(encs);
            Assert.AreEqual(3, encs.Count());

            var enc = encs.SingleOrDefault(en => en.Name == "זברה");
            Assert.IsNotNull(enc);
            Assert.AreEqual(3, enc.Id);

            var recurringEvents = enclosureController.GetRecurringEvents((int)enc.Id, (int)Languages.he);
            Assert.IsNotNull(recurringEvents);
            Assert.AreEqual(0, recurringEvents.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetRecurringEventsWrongId()
        {
            var recEvents = enclosureController.GetRecurringEvents(200);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetRecurringEventsWrongLanguage()
        {
            enclosureController.GetRecurringEvents(nonExistantLang);
        }
        #endregion

        #region DeleteRecurringEvent
        [TestMethod]
        public void DeleteRecurringEventValidInput()
        {
            var enc = enclosureController.GetAllEnclosures().SingleOrDefault(e => e.name == "קופי אדם");
            Assert.IsNotNull(enc);

            var allRecEve = enclosureController.GetRecurringEvents((int)enc.id);
            Assert.AreEqual(2, allRecEve.Count());

            var eve = allRecEve.SingleOrDefault(e => e.id == 3);
            Assert.AreEqual(eve.day, 1);
            Assert.AreEqual(eve.description, "משחק");

            enclosureController.DeleteRecurringEvent((int)eve.id);

            allRecEve = enclosureController.GetRecurringEvents((int)enc.id);
            Assert.AreEqual(1, allRecEve.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void DeleteRecuringEventIdDoesntExists()
        {
            enclosureController.DeleteRecurringEvent(1000);
        }
        #endregion

        #region UpdateEnclosure
        [TestMethod]
        public void UpdateEnclosureAddEncValidTest()
        {
            var encs = enclosureController.GetAllEnclosureResults((int)Languages.en);
            Assert.AreEqual(3, encs.Count());

            var enc = new Enclosure
            {
                id = default(int),
                name = "Lions enclosure"
            };

            enclosureController.UpdateEnclosure(enc);

            encs = enclosureController.GetAllEnclosureResults((int)Languages.en);
            Assert.AreEqual(3, encs.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateEnclosureAddTestExistingName()
        {
            var encs = enclosureController.GetAllEnclosureResults((int)Languages.en);
            Assert.IsNotNull(encs);
            Assert.AreEqual(3, encs.Count());

            var enc = new Enclosure
            {
                id = default(int),
                name = "תצוגת הקופים",
                //story = "Finalt they are here",
                //language = (int)Languages.en
            };

            enclosureController.UpdateEnclosure(enc);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateEnclosureAddTestEmptyName()
        {
            var encs = enclosureController.GetAllEnclosureResults((int)Languages.en);
            Assert.IsNotNull(encs);
            Assert.AreEqual(3, encs.Count());

            var enc = new Enclosure
            {
                id = default(int),
                name = "",
                //story = "Finalt they are here",
                //language = (int)Languages.en
            };

            enclosureController.UpdateEnclosure(enc);
        }

        [TestMethod]
        public void UpdateEnclosureValidTest()
        {
            var encs = enclosureController.GetAllEnclosures();

            Assert.IsNotNull(encs);
            Assert.AreEqual(3, encs.Count());

            var monkeyEncEn = encs.SingleOrDefault(en => en.name == "תצוגת הקופים");
            Assert.IsNotNull(monkeyEncEn);

            monkeyEncEn.name = "קקי";
            enclosureController.UpdateEnclosure(monkeyEncEn);

            encs = enclosureController.GetAllEnclosures();
            Assert.AreEqual(3, encs.Count());

            var updatedEnc = encs.SingleOrDefault(en => en.name == "קקי");
            Assert.IsNotNull(updatedEnc);

            var nothing = encs.SingleOrDefault(en => en.name == "תצוגת הקופים");
            Assert.IsNull(nothing);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateEnclosureExistingName()
        {
            var encs = enclosureController.GetAllEnclosures();
            Assert.IsNotNull(encs);
            Assert.AreEqual(3, encs.Count());

            var monkeyEncEn = encs.SingleOrDefault(en => en.name == "תצוגת הקופים");
            Assert.IsNotNull(monkeyEncEn);

            Enclosure zebra = new Enclosure
            {
                id = monkeyEncEn.id,
                markerLatitude = monkeyEncEn.markerLatitude,
                markerLongitude = monkeyEncEn.markerLongitude,
                name = "זברה"
            };

            enclosureController.UpdateEnclosure(zebra);
        }
        #endregion
    }
}