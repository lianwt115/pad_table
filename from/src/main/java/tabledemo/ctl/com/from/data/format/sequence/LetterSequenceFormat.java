package tabledemo.ctl.com.from.data.format.sequence;


import tabledemo.ctl.com.from.utils.LetterUtils;

/**
 * Created by huang on 2017/11/7.
 */

public class LetterSequenceFormat extends BaseSequenceFormat{

    @Override
    public String format(Integer position) {
        return LetterUtils.ToNumberSystem26(position);
    }
}
