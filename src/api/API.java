package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonSyntaxException;

import api.ErrorController.ErrorCase;

public class API extends HttpServlet {

	private static final long serialVersionUID = 553L;
	//
	private final String apikey = "mysecret";

	/**
	 * Executed Method, if a POST-Request has been send, checks
	 * authentifiacation-key, tries to convert posted json to object, responses with
	 * error or success to client and console
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Artikelstammdaten[] artikelstammdatenarray = null;
		final String timestamp = getTimeStamp();
		System.out.println(timestamp + "POST-Request: API");

		// check if authorisation is not NULL
		String authkey = "";
		if (request.getHeader("Authorization") != null)
			authkey = request.getHeader("Authorization");

		// verify auth-key
		if (authkey.equals(apikey)) {
			// prepare read json-string
			BufferedReader bufferedReader;
			bufferedReader = request.getReader();
			StringBuilder stringbuilder = new StringBuilder();
			String line;

			// write POST-Request into stringbuilder
			while ((line = bufferedReader.readLine()) != null) {
				stringbuilder.append(line);
				// System.out.println(line); //prints every line to console
			}

			String jsonstring = stringbuilder.toString();

			try {
				// converts Json-String into Object
				artikelstammdatenarray = Artikelstammdaten.toArtikelstammdaten(jsonstring, true);

				// checks for NULL-fields, trows NullPointerException
				for (Artikelstammdaten artikelstammdaten : artikelstammdatenarray) {
					artikelstammdaten.CheckForNull();
					// ErrorController.CheckClassFieldsForNull(artikelstammdaten);
					artikelstammdaten.CheckForSQLInjectPaddern();
				}

				// sends success-message to client
				response.getOutputStream().println(timestamp + "POST received and processed.\n" + "Contained "
						+ artikelstammdatenarray.length + " objects.");
				System.out.println("\t Content accepted, " + artikelstammdatenarray.length + " objects added.");

			} catch (JsonSyntaxException e) {
				// if string is not a json-string or wrong json construktion
				OnErrorNotifyClient_Server(response, ErrorCase.WRONG_JSON_SYNTAX, e);

			} catch (NullPointerException e) { // if checked values are empty
				if (jsonstring.isEmpty()) {
					// if sent POST-content is empty
					OnErrorNotifyClient_Server(response, ErrorCase.CONTENT_EMPTY, e);

				} else {
					// if json contained values, but did not fill all fields
					OnErrorNotifyClient_Server(response, ErrorCase.FLIELD_NULL, e);
				}

			} catch (ArrayIndexOutOfBoundsException e) {
				OnErrorNotifyClient_Server(response, ErrorCase.ARRAY_NULL, e);

			} catch (Exception e) {
				// content contained malicious/not allowed characters
				OnErrorNotifyClient_Server(response, ErrorCase.MALICIOUS_CHAR, e);
			}

			/*
			 * catch (MalformedJsonException e) {
			 * response.getOutputStream().println(timestamp +
			 * "Error[6] on processing: content was NULL");
			 * System.out.println("\tError[6] on processing: Array was empty/null\n"); }
			 */
		} else {
			// wrong or no authorisation-key has been sent in POST

			response.getOutputStream().println(timestamp + "Error[4] on processing: access denied");
			System.out.println("\tError[4] on processing: access denied, wrong or no authorisation-key set\n");
		}
		// System.out.println(artikelstammdatenarray[0].artikel_hoehe);

		// TODO whatever is going to happen to values of Artikelstammdaten will be coded
		// here
		// write into table for another valiation?

	}

	/**
	 * Creates a timestamp based on current time
	 * 
	 * @return String timestamp (format: ":::: dd/MM/yyyy :::: ")
	 */
	private String getTimeStamp() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String eventtime = ":::: " + dtf.format(LocalDateTime.now()) + " :::: ";
		return eventtime;
	}

	/**
	 * Sends response to Client and Server
	 * 
	 * @param response
	 * @param ec
	 * @param ex
	 */
	private void OnErrorNotifyClient_Server(HttpServletResponse response, ErrorCase ec, Exception ex) {
		try {
			response.getOutputStream().println(getTimeStamp() + ec);
		} catch (IOException e) {
			OnErrorNotifyServer(ErrorCase.IOERROR, e);
		}
		OnErrorNotifyServer(ec, ex);
	}

	/**
	 * Prints Line on Server-Colsole with vause of Error + exception message
	 * 
	 * @param ec
	 * @param ex
	 */
	private void OnErrorNotifyServer(ErrorCase ec, Exception ex) {
		System.out.println(ec + " : " + ex.getMessage() + "\n");
	}

	/**
	 * Executed Method, if a GET-Request has been send
	 */
	@Override
	public void doGet(HttpServletRequest requ, HttpServletResponse resp) throws ServletException, IOException {
		String timestamp = getTimeStamp();
		System.out.println(timestamp + "GET-Request: API");
		resp.getOutputStream().println("Du solltest nicht hier sein...");

	}

}
