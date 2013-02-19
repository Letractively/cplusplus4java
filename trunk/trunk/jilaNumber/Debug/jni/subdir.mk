################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../jni/com_jila_number_Dylib.cpp 

OBJS += \
./jni/com_jila_number_Dylib.o 

CPP_DEPS += \
./jni/com_jila_number_Dylib.d 


# Each subdirectory must supply rules for building sources it contributes
jni/%.o: ../jni/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: Cygwin C++ Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o"$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


