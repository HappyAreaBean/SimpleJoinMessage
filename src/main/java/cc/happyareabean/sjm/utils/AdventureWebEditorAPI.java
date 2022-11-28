package cc.happyareabean.sjm.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * The adventure-webui editor API.
 */
public final class AdventureWebEditorAPI {

	@Getter @Setter private URI root;
	private final OkHttpClient client;

	/**
	 * Creates a new instance of the editor API with the given root URI.
	 *
	 * @param root the root URI
	 */
	public AdventureWebEditorAPI(final @NotNull URI root) {
		this(root, new OkHttpClient());
	}

	/**
	 * Creates a new instance of the editor API with the given root URI and a client.
	 *
	 * @param root the root URI
	 * @param client the client
	 */
	public AdventureWebEditorAPI(final @NotNull URI root, final @NotNull OkHttpClient client) {
		this.root = Objects.requireNonNull(root, "root");
		this.client = Objects.requireNonNull(client, "client");
	}

	/**
	 * Starts a session, returning the token.
	 *
	 * @param input the input
	 * @param command the command
	 * @param application the application name
	 * @return a completable future that will provide the token
	 */
	public @NotNull CompletableFuture<String> startSession(final @NotNull String input, final @NotNull String command, final @NotNull String application) {
		final Request request = new Request.Builder()
				.url(Objects.requireNonNull(Util.convertToUrl(root.resolve("/api/editor/input"))))
				.post(RequestBody.create(constructBody(input, command, application), MediaType.parse("application/json; charset=utf-8")))
				.build();
		final CompletableFuture<String> result = new CompletableFuture<>();

		this.client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(@NotNull Call call, @NotNull IOException e) {
				result.completeExceptionally(new IOException("The result did not contain a token."));
			}

			@Override
			public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
				if (response.code() != 200) {
					result.completeExceptionally(new IOException("The server could not handle the request."));
				} else {
					final String body = response.body().string();

					JsonElement element = new JsonParser().parse(body);
					JsonElement obj = element.getAsJsonObject().get("token");
					if (!obj.isJsonNull()) {
						result.complete(obj.getAsString());
					}

					result.completeExceptionally(new IOException("The result did not contain a token."));
				}
			}
		});

		return result;
	}

	/**
	 * Retrieves the result of a session, given a token.
	 *
	 * @param token the token
	 * @return the resulting MiniMessage string in a completable future
	 */
	public @NotNull CompletableFuture<String> retrieveSession(final @NotNull String token) {
		final Request request = new Request.Builder()
				.get()
				.url(Objects.requireNonNull(Util.convertToUrl(root.resolve(URI.create("/api/editor/output?token=" + token)))))
				.build();
		final CompletableFuture<String> result = new CompletableFuture<>();

		this.client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(@NotNull Call call, @NotNull IOException e) {
				result.completeExceptionally(new IOException("The server could not handle the request."));
			}

			@Override
			public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
				final int statusCode = response.code();
				if (statusCode == 404) {
					result.complete(null);
				} else if (statusCode != 200) {
					result.completeExceptionally(new IOException("The server could not handle the request."));
				} else {
					result.complete(response.body().string());
				}
			}
		});

		return result;
	}

	private @NotNull String constructBody(final @NotNull String input, final @NotNull String command, final @NotNull String application) {
		return String.format("{\"input\":\"%s\",\"command\":\"%s\",\"application\":\"%s\"}", input, command, application);
	}
}
