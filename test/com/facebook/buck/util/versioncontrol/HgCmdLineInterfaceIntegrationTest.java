import com.google.common.base.Charsets;
      InputStreamReader diffFileReader = new InputStreamReader(diffFileStream, Charsets.UTF_8);
    List<String> lines = Files.readLines(bookmarksFile.toFile(), Charsets.UTF_8);