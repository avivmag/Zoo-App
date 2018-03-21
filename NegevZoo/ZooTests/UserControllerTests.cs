using System;
using System.Linq;
using System.Web.Http;
using Backend.Models;
using BL;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using NegevZoo.Controllers;

namespace ZooTests
{
    [TestClass]
    public class UserControllerTests
    {
        private UserController usersController;
        
        #region SetUp and TearDown
        [TestInitialize]
        public void SetUp()
        {
            // The line below must be in every setup of each test. otherwise it will not be in a testing environment.
            ControllerBase.isTesting = true;
            usersController = new UserController();
        }

        [TestCleanup]
        public void EnclosureCleanUp()
        {
            ZooContext.CleanDb();
        }

        #endregion

        #region GetAllUsers
        [TestMethod]
        public void GetAllUsersTest()
        {
            Assert.AreEqual(4, usersController.GetAllUsers().Count());
        }
        #endregion

        #region UpdateUser
        [TestMethod]
        public void UpdateUserAddAnValidTest()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new WorkerUser
            {
                Id = default(int),
                IsAdmin = false,
                Name = "gili",
                Password = "123"
            };

            usersController.UpdateUser(user);

            users = usersController.GetAllUsers();
            Assert.AreEqual(5, users.Count());
        }
        
        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserWrongNameWhiteSpaces()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new WorkerUser
            {
                Id = default(int),
                IsAdmin = false,
                Name = "     ",
                Password = "123123"
            };

            usersController.UpdateUser(user);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserWrongNameEmpty()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new WorkerUser
            {
                Id = default(int),
                IsAdmin = false,
                Name = "",
                Password = "123123"
            };

            usersController.UpdateUser(user);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserWrongPassWhiteSpaces()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new WorkerUser
            {
                Id = default(int),
                IsAdmin = false,
                Name = "גילי",
                Password = "       "
            };

            usersController.UpdateUser(user);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserWrongPassEmpty()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new WorkerUser
            {
                Id = default(int),
                IsAdmin = false,
                Name = "גילי",
                Password = ""
            };

            usersController.UpdateUser(user);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserAddNameExists()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new WorkerUser
            {
                Id = default(int),
                IsAdmin = false,
                Name = "מנהל",
                Password = "123"
            };

            usersController.UpdateUser(user);
        }

        [TestMethod]
        public void UpdateUserValidInput()
        {
            var users= usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new WorkerUser
            {
                Id = 2,
                IsAdmin = false,
                Name = "גיל",
                Password = "123"
            };

            user.Name  = "גיל המלך";

            usersController.UpdateUser(user);

            users = usersController.GetAllUsers();
            Assert.IsTrue(users.Any(a => a.Name == "גיל המלך"));
            Assert.IsFalse(users.Any(a => a.Name == "גיל"));
            Assert.AreEqual(4, users.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserNameAlreadyExists()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new WorkerUser
            {
                Id = 2,
                IsAdmin = false,
                Name = "גיל",
                Password = "123"
            };

            user.Name = "אור";

            usersController.UpdateUser(user);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserWrongId()
        {
            var users= usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new WorkerUser
            {
                Id = 2,
                IsAdmin = false,
                Name = "גיל",
                Password = "123"
            };

            user.Id = -3;

            usersController.UpdateUser(user);
        }
        #endregion

        #region DeleteUser
        [TestMethod]
        public void DeleteUserValidInput()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = users.SingleOrDefault(en => en.Name == "עובד");
            Assert.IsNotNull(user);

            usersController.DeleteUser(user.Id);
            users = usersController.GetAllUsers();

            Assert.AreEqual(3, users.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void DeleteAnimalIdDoesntExists()
        {
            usersController.DeleteUser(-4);
        }

        #endregion
    }
}
