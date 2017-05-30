using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.IO;
using System.IO.Compression;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Forms;


namespace lab_5
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {

        int n1 = 1, n2 = 1;
        int licznik = 0;
        public MainWindow()
        {
            InitializeComponent();
            //asynchronicNewton();
        }
        private Tuple<int, int> NewtonInput()
        {
            return new Tuple<int, int>(
                Int32.Parse(nBox.Text), Int32.Parse(kBox.Text));
        }

        private int Licznik(Tuple<int, int> input)
        {
            int n = input.Item1;
            int k = input.Item2;

            int result = 1;

            for (int i = n - k + 1; i <= n; i++)
            {
                result *= i;
            }

            return result;
        }
        private int Mianownik(Tuple<int, int> input)
        {
            int n = input.Item1;
            int k = input.Item2;

            int result = 1;

            for (int i = 1; i <= k; i++)
            {
                result *= i;
            }

            return result;
        }
        private double DwumianNewtona(Tuple<int, int> input)
        {
            Task<int> numerTask = Task.Run(() => Licznik(input));
            Task<int> denomTask = Task.Run(() => Mianownik(input));
            return (double)numerTask.Result / denomTask.Result;
        }

        private void task_button_Click(object sender, RoutedEventArgs e)
        {
            taskBox.Text = DwumianNewtona(NewtonInput()).ToString();
        }

        private void delegate_button_Click(object sender, RoutedEventArgs e)
        {
            Func<Tuple<int, int>, double> op = DwumianNewtona;
            IAsyncResult result = op.BeginInvoke(NewtonInput(), null, null);
            double newton = op.EndInvoke(result);
            delBox.Text = newton.ToString();
        }
        private int licznik2()
        {
            int n = n1;
            int k = n2;

            int result = 1;

            for (int i = n - k + 1; i <= n; i++)
            {
                result *= i;
            }
            Thread.Sleep(10000);
            return result;
        }
        private async void change()
        {
            Task<int> task1 = new Task<int>(licznik2);
            task1.Start();
            Task<int> task2 = new Task<int>(mianownik2);
            task2.Start();
            //int l = await licznik2();
            int l = await task1;
            int m = await task2;
            asyBox.Text = ((double)l / m).ToString();

        }
        private int mianownik2()
        {
            int k = n2;

            int result = 1;

            for (int i = 1; i <= k; i++)
            {
                result *= i;
            }
            Thread.Sleep(10000);
            return result;
        }
        private void BGworker()
        {
            int j = Int32.Parse(textBox6.Text);
            BackgroundWorker bw = new BackgroundWorker();
            bw.DoWork += ((object sender, DoWorkEventArgs e) =>
            {
                BackgroundWorker worker = sender as BackgroundWorker;
                int iDest = (int)e.Argument;

                decimal prev = 0;
                decimal curr = 1;

                for (int i = 1; i <= iDest - 1; i++)
                {
                    Thread.Sleep(20);

                    decimal newCurr = prev + curr;
                    prev = curr;
                    curr = newCurr;

                    worker.ReportProgress(100 * i / iDest);
                }

                e.Result = curr;
            });
            bw.ProgressChanged += ((object sender, ProgressChangedEventArgs e) =>
            {
                progressBar.Value = e.ProgressPercentage;
            });
            bw.RunWorkerCompleted += ((object sender, RunWorkerCompletedEventArgs e) =>
            {
                textBox7.Text = e.Result.ToString();
            });
            bw.WorkerReportsProgress = true;
            bw.RunWorkerAsync(j);
        }

        private void get_button_Click(object sender, RoutedEventArgs e)
        {
            BGworker();
        }

        private void async_button_Click(object sender, RoutedEventArgs e)
        {
            n1 = Int32.Parse(nBox.Text); n2 = Int32.Parse(kBox.Text);
            change();
        }


        private void check_button_click(object sender, RoutedEventArgs e)
        {
            licznik++;
            textBox3_Copy1.Text = licznik.ToString();
        }
        public static void Compress(DirectoryInfo dir)
        {
            Parallel.ForEach(dir.GetFiles(), (toCompress) =>
            {
                using (FileStream toCompressStream = toCompress.OpenRead())
                {
                    using (FileStream compressedFileStream = File.Create(toCompress.FullName + ".gz"))
                    {
                        using (GZipStream compressionStream = new GZipStream(
                            compressedFileStream, CompressionMode.Compress))
                        {
                            toCompressStream.CopyTo(compressionStream);
                        }
                    }
                }
            });
        }

        public static void Decompress(DirectoryInfo dir)
        {
            Parallel.ForEach(dir.GetFiles(), (toDecompress) => {
                if (toDecompress.Extension == ".gz")
                {
                    using (FileStream compressedStream = toDecompress.OpenRead())
                    {
                        string name = toDecompress.FullName;
                        string newName = name.Remove(name.Length - toDecompress.Extension.Length);

                        using (FileStream decompressedStream = File.Create(newName))
                        {
                            using (GZipStream decompressionStream = new GZipStream(
                                compressedStream, CompressionMode.Decompress))
                            {
                                decompressionStream.CopyTo(decompressedStream);
                            }
                        }
                    }
                }
            });
        }
        private void DnsClick(object sender, EventArgs e)
        {
            string[] hostNames = {
                "www.microsoft.com",
                "www.apple.com", "www.google.com",
                "www.ibm.com", "cisco.netacad.net",
                "www.oracle.com", "www.nokia.com",
                "www.hp.com", "www.dell.com",
                "www.samsung.com", "www.toshiba.com",
                "www.siemens.com", "www.amazon.com",
                "www.sony.com", "www.canon.com",
                "www.alcatel-lucent.com", "www.acer.com",
                "www.motorola.com"
            };

            dnsBox.Text = hostNames.AsParallel()
                .Select(h => new { Host = h, IP = Dns.GetHostAddresses(h)[0] })
                .Aggregate("", (accum, host) => accum + host.Host + " => " + host.IP + "\r\n");
        }
        private void compressClick(object sender, EventArgs e)
        {
            var dialog = new System.Windows.Forms.FolderBrowserDialog() { Description = "Select directory to open" };
            dialog.ShowDialog();
            Compress(new DirectoryInfo(dialog.SelectedPath));

        }

        private void decompressClick(object sender, EventArgs e)
        {
            var dialog = new System.Windows.Forms.FolderBrowserDialog() { Description = "Select directory to open" };
            dialog.ShowDialog();
            Decompress(new DirectoryInfo(dialog.SelectedPath));
        }
    }
}
