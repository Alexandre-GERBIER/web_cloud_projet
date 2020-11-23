package insta;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import insta.datastore.PostEntity;
import insta.datastore.UserEntity;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class Misc {

    public static List<Entity> parseQuery(List<Entity> input){
        return quickSort(input);

    }


    public static List<Entity> quickSort(List<Entity> array) {
        if (array.isEmpty())
            return array;
        else {
            Entity pivot = array.get(0);

            List<Entity> less = new LinkedList<Entity>();
            List<Entity> pivotList = new LinkedList<Entity>();
            List<Entity> more = new LinkedList<Entity>();

            // Partition
            for (Entity i: array) {
                Timestamp ts = (Timestamp) i.getProperty("timestamp");
                Timestamp tsPivot = (Timestamp) pivot.getProperty("timestamp");
                if (ts.before(tsPivot))
                    less.add(i);
                else if (ts.after(tsPivot))
                    more.add(i);
                else
                    pivotList.add(i);
            }

            // Recursively sort sublists
            less = quickSort(less);
            more = quickSort(more);

            // Concatenate results
            less.addAll(pivotList);
            less.addAll(more);
            return less;
        }
    }


    public static void fillDatabase() throws EntityNotFoundException {
        LinkedList<String> firstName = new LinkedList<String>();
        LinkedList<String> lastName1 = new LinkedList<String>();
        LinkedList<String> lastName2 = new LinkedList<String>();
        LinkedList<Key> users = new LinkedList<>();
        String mail = "@fakemail.nantes";

        firstName.add("Jake");
        firstName.add("Andy");
        firstName.add("John");
        firstName.add("Charles");
        firstName.add("Terry");
        firstName.add("Raymond");
        firstName.add("Gina");
        firstName.add("Amy");
        firstName.add("Rosa");
        firstName.add("Eliott");

        lastName1.add("Smith");
        lastName1.add("Patel");
        lastName1.add("Peralta");
        lastName1.add("Boyle");
        lastName1.add("Jeffords");
        lastName1.add("Holt");
        lastName1.add("Linetti");
        lastName1.add("Santiago");
        lastName1.add("Diaz");
        lastName1.add("Alderson");

        lastName2.add("Peralta");
        lastName2.add("Boyle");
        lastName2.add("Holt");
        lastName2.add("Santiago");
        lastName2.add("Diaz");
        lastName2.add("Alderson");

        for(String name : firstName){
            for(String name1 : lastName1) {
                for (String name2 : lastName2) {
                    String lastNameTmp = name1 + '-' + name2 ;
                    Key newUser =(UserEntity.createUser(name, lastNameTmp , name + '.' + lastNameTmp + mail, "", "", name + '-' + lastNameTmp));
                    UserEntity.googleAuthentification(name + '-' + name1 + '-' + name2);
                    users.add(newUser);
                    //every user has one post
                    PostEntity.createPost(newUser,"image "+name+" "+lastNameTmp,"description");
                    int usersCreated = users.size();
                    if(usersCreated > 1 ){
                        //first user followed by everyone (599 pers)
                        UserEntity.follow(newUser, users.getFirst());
                    }
                    if(usersCreated > 2 && usersCreated < 103){
                        UserEntity.follow(newUser, users.get(2));
                    }
                    if(usersCreated > 3 && usersCreated < 14){
                        UserEntity.follow(newUser, users.get(3));
                    }
                }
            }
        }


    }

}
