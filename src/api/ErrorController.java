package api;

public class ErrorController {

	public enum ErrorCase {
		WRONG_JSON_SYNTAX(1, "Wrong syntax"), CONTENT_EMPTY(21, "Content empty"), FLIELD_NULL(22, "Field is null"),
		ARRAY_NULL(23, "Array is null"), MALICIOUS_CHAR(3, "Malicious character found"),
		AUTH_ERROR(4, "Access Denied, no/wrong authentification key"), IOERROR(5, "I/O-Error");

		private final Integer nr;
		private final String description;

		private ErrorCase(Integer nr, String description) {
			this.nr = nr;
			this.description = description;
		}

		@Override
		public String toString() {
			return "Error[" + nr + "] : " + description;
		}
	}

	private String message = "";

	/**
	 * Checks object for NULL values, throws custom NullPointerException
	 * 
	 * @param <T>
	 */
	public static <T> void CheckClassFieldsForNull(T thing) {
		java.lang.reflect.Field[] fields = thing.getClass().getFields();
		for (java.lang.reflect.Field field : fields) {
			requireNonNull(field);
		}

	}

	private static <T> void requireNonNull(T obj) {
		if (obj == null)
			throw new NullPointerException();
	}
}
