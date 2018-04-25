using BL;
using DAL;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Linq;

namespace ZooTests.Unit_Tests
{
    [TestClass]
    public class UserUnitTests
    {
        private ZooContext context;

        #region SetUp and TearDown
        [TestInitialize]
        public void SetUp()
        {
            context = new ZooContext();
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
            Assert.AreEqual(4, context.GetAllUsers().Count());
        }
        #endregion

        #region Login

        [TestMethod]
        public void LoginValidTest()
        {
            var users = context.GetAllUsers();

            var orUser = users.SingleOrDefault(u => u.name == "אור");

            Assert.IsNotNull(orUser);
            Assert.IsTrue(context.Login(orUser.name, "123"));
        }

        [TestMethod]
        public void LoginWrongPassword()
        {
            var users = context.GetAllUsers();

            var orUser = users.SingleOrDefault(u => u.name == "אור");

            Assert.IsNotNull(orUser);
            Assert.IsFalse(context.Login(orUser.name, "123a"));
        }

        [TestMethod]
        public void LoginWrongUserName()
        {
            var users = context.GetAllUsers();

            var orUser = users.SingleOrDefault(u => u.name == "אור");

            Assert.IsNotNull(orUser);
            Assert.IsFalse(context.Login(orUser.name + "k", "123a"));
        }
        #endregion

        #region GetUserByNameAndPass

        [TestMethod]
        public void GetUserByNameAndPassValidTest()
        {
            var orUser = context.GetUserByNameAndPass("אור", "123");

            Assert.IsNotNull(orUser);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Can't find a user with this name and password")]
        public void GetUserByNameAndPassWrongName()
        {
            var orUser = context.GetUserByNameAndPass("אור" + "g", "123");
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Can't find a user with this name and password")]
        public void GetUserByNameAndPassWrongPass()
        {
            var orUser = context.GetUserByNameAndPass("אור", "123a");
        }

        #endregion

        #region UpdateUser

        [TestMethod]
        public void UpdateUserAddAnValidTest()
        {
            var users = context.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = default(int),
                isAdmin = false,
                name = "gili",
                password = "123"
            };

            context.UpdateUser(user);

            users = context.GetAllUsers();
            Assert.AreEqual(5, users.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The user name is empty or white spaces")]
        public void UpdateUserWrongNameWhiteSpaces()
        {
            var users = context.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = default(int),
                isAdmin = false,
                name = "     ",
                password = "123123"
            };

            context.UpdateUser(user);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The user name is empty or white spaces")]
        public void UpdateUserWrongNameEmpty()
        {
            var users = context.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = default(int),
                isAdmin = false,
                name = "",
                password = "123123"
            };

            context.UpdateUser(user);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The password is empty or white spaces")]
        public void UpdateUserWrongPassWhiteSpaces()
        {
            var users = context.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = default(int),
                isAdmin = false,
                name = "גילי",
                password = "       "
            };

            context.UpdateUser(user);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The password is empty or white spaces")]
        public void UpdateUserWrongPassEmpty()
        {
            var users = context.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = default(int),
                isAdmin = false,
                name = "גילי",
                password = ""
            };

            context.UpdateUser(user);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input while adding a User. Name already exists")]
        public void UpdateUserAddNameExists()
        {
            var users = context.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = default(int),
                isAdmin = false,
                name = "מנהל",
                password = "123"
            };

            context.UpdateUser(user);
        }

        [TestMethod]
        public void UpdateUserValidInput()
        {
            var users = context.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = 2,
                isAdmin = false,
                name = "גיל",
                password = "123"
            };

            user.name = "גיל המלך";

            context.UpdateUser(user);

            users = context.GetAllUsers();
            Assert.IsTrue(users.Any(a => a.name == "גיל המלך"));
            Assert.IsFalse(users.Any(a => a.name == "גיל"));
            Assert.AreEqual(4, users.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input while updating a User. Name already exists")]
        public void UpdateUserNameAlreadyExists()
        {
            var users = context.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = 2,
                isAdmin = false,
                name = "גיל",
                password = "123"
            };

            user.name = "אור";

            context.UpdateUser(user);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. User doesn't exists")]
        public void UpdateUserWrongId()
        {
            var users = context.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = 2,
                isAdmin = false,
                name = "גיל",
                password = "123"
            };

            user.id = -3;

            context.UpdateUser(user);
        }
        #endregion

        #region DeleteUser

        [TestMethod]
        public void DeleteUserValidInput()
        {
            var users = context.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = users.SingleOrDefault(en => en.name == "עובד");
            Assert.IsNotNull(user);

            context.DeleteUser((int)user.id);
            users = context.GetAllUsers();

            Assert.AreEqual(3, users.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. User ID doesn't exists.")]
        public void DeleteAnimalIdDoesntExists()
        {
            context.DeleteUser(-4);
        }

        #endregion
    }
}
