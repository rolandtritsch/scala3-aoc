package aoc0000.util

/** A typed, recoverable failure while reading or parsing a day's input.
  *
  * Used as the error type for every `readFile` effect and for pure parsing functions that can fail;
  * never represented as a thrown exception.
  */
enum AocError:
  /** The named resource could not be read. */
  case FileReadError(filename: String, cause: Throwable)

  /** A line or value in the named resource did not match the expected format. */
  case ParseError(filename: String, message: String)

  /** An already-read value failed a domain rule (e.g. an empty secret key). */
  case ValidationError(message: String)
end AocError
