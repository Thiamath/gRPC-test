package com.thiamath.user.client;

import com.diogonunes.jcolor.Ansi;
import com.diogonunes.jcolor.Attribute;
import com.thiamath.user.UserOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Controller {

    private final Random random = new Random();

    private final UserClient client;

    public Controller(ManagedChannel channel) {
        client = new UserClient(channel);
    }

    public void getUser(final String userName) {
        try {
            final UserOuterClass.User user = client.getUser(userName);
            System.out.println("Got user:\n" +
                    "{\n" +
                    "  username: " + Ansi.colorize(user.getName(), Attribute.CYAN_TEXT()) + "\n" +
                    "  dob: " + Ansi.colorize(user.getAge() + "", Attribute.CYAN_TEXT()) + "\n" +
                    "}");
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getDescription() != null) {
                System.out.println(Ansi.colorize(e.getStatus().getDescription(), Attribute.RED_TEXT()));
            }
            System.out.println(Ansi.colorize(e.getMessage(), Attribute.RED_TEXT()));
        }
    }

    public void getUserList() {
        Iterable<UserOuterClass.User> userList = client.getUserList();
        System.out.println(userList);
    }

    public void sendUser(final String username) {
        final UserOuterClass.User user = UserOuterClass.User.newBuilder()
                .setName(username)
                .setAge((random.nextInt() % 40) + 20)
                .build();
        client.sendUser(user);
    }

    public void sendUserList(String[] usernameList) {
        List<UserOuterClass.User> userList = Arrays.stream(usernameList)
                .map((username) -> UserOuterClass.User.newBuilder().setName(username).setAge((new Random().nextInt() % 40) + 20).build())
                .collect(Collectors.toUnmodifiableList());
        client.sendUserList(userList);
    }

    public UserClient.Chat startChat() {
        return client.conversation();
    }
}
