using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Diagnostics;
using System.Net.Http;
using System.Net.Http.Headers;

namespace ZooTests.NonFunctional_Tests
{
    [TestClass]
    public class LoginTest
    {

        [TestMethod]
        public void LoginTimeTest()
        {
            HttpClient client = new HttpClient();
            var timer = new Stopwatch();
            client.BaseAddress = new Uri("http://negevZoo.Sytes.net:50555/");
            timer.Start();
            client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
            var resp2 = client.GetAsync("users/login/gil/gil").Result;
            timer.Stop();
            Assert.IsTrue(resp2.IsSuccessStatusCode);
            Assert.IsTrue(2000 > timer.ElapsedMilliseconds);
        }
    }
}
