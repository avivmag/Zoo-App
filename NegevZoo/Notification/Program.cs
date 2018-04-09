using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Web.Script.Serialization;

namespace Notification
{
    class Program
    {
        private static string deviceId = "eN0AceUN7UU:APA91bFZSmLewxCsT13KqymCRHliez5Sne_RQIf_WgZFD88ipMgllXLsF7VnAQcfNgXiAbnfpN1iYSJBJXNljXNLI1ad8lS4yxmNPAOOYoexkNhva0dljXeB01U8DO4eEjaeNqQctHOM";
        private static string applicationID = "1:777829984351:android:996e3b073e4dadd3";
        private static string senderId = "777829984351";
        private static string serverKey = "AAAAtRpHqF8:APA91bHQd7MJTMz-_dNhAU-kfLCJO-WYZAuqAQ2u4Z0evwW9K69DWcAXHoW5rPkr71LU_6SEkRUY2Op95qwNe8gkkZtCWQDDQwJf7TMJrrB8dYmdhu6s0doCjokrWxXngPgAcXeLTcMS";

        public static void Main(string[] args)
        {
            var kaki = NotifyAsync(deviceId, "Kaki", "עכשיו בעברית");

            kaki.Wait();
        }

        public static async Task<bool> NotifyAsync(string to, string title, string body)
        {
            try
            {
                // Get the server key from FCM console
                var formatServerKey = string.Format("key={0}", serverKey);

                // Get the sender id from FCM console
                var formatSenderId = string.Format("id={0}", senderId);

                var data = new
                {
                    to, // Recipient device token
                    notification = new { title, body }
                };

                var serializer = new JavaScriptSerializer();
                var jsonBody = serializer.Serialize(data);

                using (var httpRequest = new HttpRequestMessage(HttpMethod.Post, "https://fcm.googleapis.com/fcm/send"))
                {
                    httpRequest.Headers.TryAddWithoutValidation("Authorization", formatServerKey);
                    httpRequest.Headers.TryAddWithoutValidation("Sender", formatSenderId);
                    httpRequest.Content = new StringContent(jsonBody, Encoding.UTF8, "application/json");

                    using (var httpClient = new HttpClient())
                    {
                        var result = await httpClient.SendAsync(httpRequest);

                        if (result.IsSuccessStatusCode)
                        {
                            Console.WriteLine("Success!");
                            Console.ReadLine();
                            return true;
                        }
                        else
                        {
                            // Use result.StatusCode to handle failure
                            // Your custom error handler here
                            Console.WriteLine($"Error sending notification. Status Code: {result.StatusCode}");
                            Console.ReadLine();
                        }
                    }
                }
                Console.WriteLine("WHATTTT!");
                Console.ReadLine();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Exception thrown in Notify Service: {ex}");
                Console.ReadLine();
            }
            Console.WriteLine("WHATTTT 2!");
            Console.ReadLine();
            return false;
        }

        public static void Notification(string message)
        {
            try
            {
                WebRequest tRequest = WebRequest.Create("https://fcm.googleapis.com/fcm/send");
                tRequest.Method = "POST";
                tRequest.ContentType = "application/json";
                var data = new
                {
                    to = deviceId,
                    notification = new
                    {
                        body = "Mother of Gili",
                        title = message,
                        sound = "Enabled"
                    }
                };

                var serializer = new JavaScriptSerializer();
                var json = serializer.Serialize(data);
                Byte[] byteArray = Encoding.UTF8.GetBytes(json);
                tRequest.Headers.Add(string.Format("Authorization: key={0}", applicationID));
                tRequest.Headers.Add(string.Format("Sender: id={0}", senderId));
                tRequest.ContentLength = byteArray.Length;

                using (Stream dataStream = tRequest.GetRequestStream())
                {
                    dataStream.Write(byteArray, 0, byteArray.Length);
                    using (WebResponse tResponse = tRequest.GetResponse())
                    {
                        using (Stream dataStreamResponse = tResponse.GetResponseStream())
                        {
                            using (StreamReader tReader = new StreamReader(dataStreamResponse))
                            {
                                String sResponseFromServer = tReader.ReadToEnd();
                                string str = sResponseFromServer;
                            }
                        }
                    }
                }

                Console.WriteLine("DONE!");
                Console.ReadLine();
            }
            catch (Exception ex)
            {
                string str = ex.Message;
                Console.WriteLine(str);
                Console.ReadLine();
            }
        }
    }
}
