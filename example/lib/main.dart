import 'dart:io';
import 'dart:async';

import 'package:flutter/material.dart';

import 'package:path_provider/path_provider.dart';
import 'package:pdf/pdf.dart';
import 'package:pdf/widgets.dart' as pw;
import 'package:path/path.dart' as p;

import 'package:file_edit_launcher/file_edit_launcher.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  MyAppState createState() => MyAppState();
}

class MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
  }

  Future<void> launchEditFile() async {
    var dir = await getApplicationDocumentsDirectory();
    var dirPath = dir.path;
    var file = File(p.join(dirPath, "filename.pdf"));
    var pdf = createPdf();
    await file.writeAsBytes(await pdf.save());

    await FileEditLauncher.launchFileEditor(file);
  }

  pw.Document createPdf() {
    final pdf = pw.Document();

    pdf.addPage(pw.Page(
        pageFormat: PdfPageFormat.a4,
        build: (pw.Context context) {
          return pw.Center(
            child: pw.Text(
                "This is a test of your local emergency broadcast system."),
          ); // Center
        })); // Page

    return pdf;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
            child: TextButton(
          onPressed: launchEditFile,
          child: const Text('Edit File'),
        )),
      ),
    );
  }
}
