package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;

    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }


    public String createUser(String name, String mobile) throws Exception {
        //if user already exist throws an exception
        if(userMobile.contains(mobile)){
            throw new Exception("User already exists");
        }
        //otherwise add new user
        User user=new User(name,mobile);
        userMobile.add(user.getMobile());
        return "SUCCESS";
    }

    public Group createGroup(List<User> users){
        if(users.size()>2){
            //if no of users in grp is greater than 2,then grp name should be grp count
            customGroupCount++;
            Group group = new Group("Group "+customGroupCount, users.size());
            groupUserMap.put(group, users);
            adminMap.put(group, users.get(0));
            groupMessageMap.put(group, new ArrayList<Message>());
            return group;
        }
        Group group = new Group(users.get(1).getName(), users.size());
        groupUserMap.put(group, users);
        adminMap.put(group, users.get(0));
        groupMessageMap.put(group, new ArrayList<Message>());
        return group;
    }

    public int createMessage(String content){
        messageId++;
        Message message=new Message(messageId,content);

        return messageId;
    }
    public int sendMessage(Message message, User sender, Group group) throws Exception{
        //if grp doesn't exist it throws exception
        if(!groupUserMap.containsKey(group))
        {
            throw new Exception("Group does not exist");
        }
        //if sender is not in grp it throws exception
        if(!groupUserMap.get(group).contains(sender))
        {
            throw new Exception("You are not allowed to send message");
        }
        senderMap.put(message, sender);
        List<Message>ml=groupMessageMap.get(group);
        ml.add(message);
        groupMessageMap.put(group,ml);
        return ml.size();  //sending final no of msg in grp
    }
    public String changeAdmin(User approver, User user, Group group) throws Exception{
        // //if grp doesn't exist it throws exception
        if(!groupUserMap.containsKey(group))
        {
            throw new Exception("Group does not exist");
        }
        //If the approver is not the current admin of the group, the application will throw an exception.
        if(!approver.equals(adminMap.get(group)))
        {
            throw new Exception("Approver does not have rights");
        }
        //if user is not part of the grp
        if(!groupUserMap.get(group).contains(user))
        {
            throw new Exception("User is not a participant");
        }
        //new admin
        adminMap.put(group,user);

        return "SUCCESS";
    }
//    public int removeUser(User user) throws Exception{
//        return
//    }
//
//    public String findMessage(Date start, Date end, int K) throws Exception{
//
//        return
//    }


}
