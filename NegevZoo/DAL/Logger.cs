using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Security.AccessControl;
using System.Text;
using System.Threading.Tasks;

namespace DAL
{
    public class Logger
    {
        private static Logger logger;
        private static String filePath;
        private static DateTime date;

        protected Logger(string path = null)
        {
            date        = DateTime.Today;
            filePath    =  path ?? String.Format(Properties.Settings.Default.PathLog+"{0}.log", date.ToString("yyyy-MM-dd"));
        }

        public static Logger GetInstance(bool isTesting)
        {
            if (logger == null || date != DateTime.Today)
            {
                //settig the path to testing if needed
                string path = null;
                if (isTesting)
                {
                    path = String.Format(Properties.Settings.Default.TestLog+"{0}.log", DateTime.Today.ToString("yyyy-MM-dd"));
                }
                logger      = new Logger(path);

            }

            return logger;
        }

        public String ReadLine()
        {
            try
            {
                var stream = File.Open(filePath, FileMode.Open, FileAccess.Read);

                StreamReader reader = new StreamReader(stream);
                var line = reader.ReadLine();
                reader.Dispose();
                return line;
            }
            catch
            {
                return null;
            }
        }


        public void WriteLine(params String[] messages)
        {
            try
            {
                FileStream stream = null;

                if (!File.Exists(filePath))
                {
                    stream = File.Create(filePath);
                    
                    var account = "Users";
                    var rights = FileSystemRights.Write;
                    var controlType = AccessControlType.Allow;
                    // Get a FileSecurity object that represents the
                    // current security settings.
                    FileSecurity fSecurity = File.GetAccessControl(filePath);

                    // Add the FileSystemAccessRule to the security settings.
                    fSecurity.AddAccessRule(new FileSystemAccessRule(account, rights, controlType));

                    // Set the new access settings.
                    File.SetAccessControl(filePath, fSecurity);
                }
                else
                {
                    stream = File.Open(filePath, FileMode.Append, FileAccess.Write);
                }

                StreamWriter writer = new StreamWriter(stream);
                writer.WriteLine(DateTime.Now);
                foreach (String message in messages)
                {
                    writer.WriteLine(message);
                }
                writer.WriteLine();
                writer.WriteLine();
                writer.Dispose();
                stream.Dispose();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }

        }
    
        
        public class LoggerRec : Logger
        {
            private static Logger logger_rec;
            private static String filePath_rec;
            private static DateTime date_rec;

            protected LoggerRec(string path = null)
            {
                date_rec = DateTime.Today;
                filePath = path ?? String.Format(Properties.Settings.Default.RecLog + "{0}.log", date_rec.ToString("yyyy-MM-dd"));
            }

            public static Logger GetLoggerRecInstance()
            {
                if (logger_rec == null || date_rec != DateTime.Today)
                {
                    filePath_rec = String.Format(Properties.Settings.Default.RecLog + "{0}.log", DateTime.Today.ToString("yyyy-MM-dd"));
                    logger_rec = new LoggerRec(filePath_rec);
                }

                return logger_rec;
            }
        }
    }

}
