﻿using System;
using System.Linq;
using System.Web.Http;
using DAL;
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
            DummyDB.CleanDb();
        }

        #endregion

        #region GetAllUsers
        [TestMethod]
        public void GetAllUsersTest()
        {
            Assert.AreEqual(4, usersController.GetAllUsers().Count());
        }
        #endregion

        #region Login
        [TestMethod]
        public void LoginValidTest()
        {
            var users = usersController.GetAllUsers();

            var orUser = users.SingleOrDefault(u => u.name == "אור");

            Assert.IsNotNull(orUser);
            Assert.IsTrue(usersController.Login(orUser.name, "123"));
        }

        [TestMethod]
        public void LoginWrongPassword()
        {
            var users = usersController.GetAllUsers();

            var orUser = users.SingleOrDefault(u => u.name == "אור");

            Assert.IsNotNull(orUser);
            Assert.IsFalse(usersController.Login(orUser.name, "123a"));
        }

        [TestMethod]
        public void LoginWrongUserName()
        {
            var users = usersController.GetAllUsers();

            var orUser = users.SingleOrDefault(u => u.name == "אור");

            Assert.IsNotNull(orUser);
            Assert.IsFalse(usersController.Login(orUser.name + "k", "123a"));
        }
        #endregion

        #region GetUserByNameAndPass
        [TestMethod]
        public void GetUserByNameAndPassValidTest()
        {
            var orUser= usersController.GetUserByNameAndPass("אור","123");

            Assert.IsNotNull(orUser);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetUserByNameAndPassWrongName()
        {
            var orUser = usersController.GetUserByNameAndPass("אור"+"g", "123");
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetUserByNameAndPassWrongPass()
        {
            var orUser = usersController.GetUserByNameAndPass("אור", "123a");
        }

        #endregion
        
        #region UpdateUser
        [TestMethod]
        public void UpdateUserAddAnValidTest()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = default(int),
                isAdmin = false,
                name = "gili",
                password = "123"
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

            var user = new User
            {
                id = default(int),
                isAdmin = false,
                name = "     ",
                password = "123123"
            };

            usersController.UpdateUser(user);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserWrongNameEmpty()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = default(int),
                isAdmin = false,
                name = "",
                password = "123123"
            };

            usersController.UpdateUser(user);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserWrongPassWhiteSpaces()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = default(int),
                isAdmin = false,
                name = "גילי",
                password = "       "
            };

            usersController.UpdateUser(user);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserWrongPassEmpty()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = default(int),
                isAdmin = false,
                name = "גילי",
                password = ""
            };

            usersController.UpdateUser(user);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserAddNameExists()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = default(int),
                isAdmin = false,
                name = "מנהל",
                password = "123"
            };

            usersController.UpdateUser(user);
        }

        [TestMethod]
        public void UpdateUserValidInput()
        {
            var users= usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = 2,
                isAdmin = false,
                name = "גיל",
                password = "123"
            };

            user.name  = "גיל המלך";

            usersController.UpdateUser(user);

            users = usersController.GetAllUsers();
            Assert.IsTrue(users.Any(a => a.name == "גיל המלך"));
            Assert.IsFalse(users.Any(a => a.name == "גיל"));
            Assert.AreEqual(4, users.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserNameAlreadyExists()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = 2,
                isAdmin = false,
                name = "גיל",
                password = "123"
            };

            user.name = "אור";

            usersController.UpdateUser(user);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserWrongId()
        {
            var users= usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = 2,
                isAdmin = false,
                name = "גיל",
                password = "123"
            };

            user.id = -3;

            usersController.UpdateUser(user);
        }
        #endregion

        #region DeleteUser
        [TestMethod]
        public void DeleteUserValidInput()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = users.SingleOrDefault(en => en.name == "עובד");
            Assert.IsNotNull(user);

            usersController.DeleteUser((int)user.id);
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
