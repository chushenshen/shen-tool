package io.github.cshencode.util.list.fill;


import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * <p>
 * -  转换组
 * </p>
 *
 * @author css
 * @since 2022/11/22
 */
public class ConvertGroup<SOURCE, TARGET, I> {
    private SFunction<TARGET, ?> targetNameGetFunction;
    private BiConsumer<SOURCE, I> sourceNameSetFunction;

    private BiFunction<Object, Map<I, TARGET>, I> compareHandler;

    private I defaultValue;

    public ConvertGroup() {
    }

    public static <SOURCE, TARGET, I> ConvertGroup<SOURCE, TARGET, I> cv(SFunction<TARGET, ?> targetNameGetFunction, BiConsumer<SOURCE, I> sourceNameSetFunction) {
        ConvertGroup<SOURCE, TARGET, I> convertGroup = new ConvertGroup<>();
        convertGroup.setTargetNameGetFunction(targetNameGetFunction);
        convertGroup.setSourceNameSetFunction(sourceNameSetFunction);
        return convertGroup;
    }

    public static <SOURCE, TARGET, I> ConvertGroup<SOURCE, TARGET, I> cv(SFunction<TARGET, ?> targetNameGetFunction, BiConsumer<SOURCE, I> sourceNameSetFunction, BiFunction<Object, Map<I, TARGET>, I> compareHandler) {
        ConvertGroup<SOURCE, TARGET, I> convertGroup = new ConvertGroup<>();
        convertGroup.setTargetNameGetFunction(targetNameGetFunction);
        convertGroup.setSourceNameSetFunction(sourceNameSetFunction);
        convertGroup.setCompareHandler(compareHandler);
        return convertGroup;
    }

    public static <SOURCE, TARGET, I> ConvertGroup<SOURCE, TARGET, I> cv(SFunction<TARGET, ?> targetNameGetFunction, BiConsumer<SOURCE, I> sourceNameSetFunction, I defaultValue) {
        ConvertGroup<SOURCE, TARGET, I> convertGroup = new ConvertGroup<>();
        convertGroup.setTargetNameGetFunction(targetNameGetFunction);
        convertGroup.setSourceNameSetFunction(sourceNameSetFunction);
        convertGroup.defaultValue = defaultValue;
        return convertGroup;
    }

    public SFunction<TARGET, ?> getTargetNameGetFunction() {
        return targetNameGetFunction;
    }

    public void setTargetNameGetFunction(SFunction<TARGET, ?> targetNameGetFunction) {
        this.targetNameGetFunction = targetNameGetFunction;
    }

    public BiConsumer<SOURCE, I> getSourceNameSetFunction() {
        return sourceNameSetFunction;
    }

    public void setSourceNameSetFunction(BiConsumer<SOURCE, I> sourceNameSetFunction) {
        this.sourceNameSetFunction = sourceNameSetFunction;
    }

    public BiFunction<Object, Map<I, TARGET>, I> getCompareHandler() {
        return compareHandler;
    }

    public void setCompareHandler(BiFunction<Object, Map<I, TARGET>, I> compareHandler) {
        this.compareHandler = compareHandler;
    }
}
