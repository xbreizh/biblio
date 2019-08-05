package org.library.helper;

import org.library.model.Member;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public interface LibraryHelper {

    String getConnectedLogin();

    String getConnectedToken();

    void addingPopup(ModelAndView mv, String error);

    void checkMaxReserved(Member member, ModelAndView mv);

    void checkOverdue(Member member, ModelAndView mv);

    void getIsbnRentedList(Member member, ModelAndView mv);

    Map<String, String> generateSearchMap(String isbn, String author, String title);

}
