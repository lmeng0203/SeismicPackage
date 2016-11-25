!!------------------------ avovan_wrap.f90 --------------------------!!
!!------------------------ avovan_wrap.f90 --------------------------!!
!!------------------------ avovan_wrap.f90 --------------------------!!

!!!!!!!!!! DO NOT EDIT THIS FILE - it is machine generated !!!!!!!!!!

!!------------------------------ module -----------------------------------!!
!!------------------------------ module -----------------------------------!!
!!------------------------------ module -----------------------------------!!

      module avovan_wrap_module
      use avovan_module
      implicit none
      public

      type :: avovan_wrap_struct
        type(avovan_struct),pointer :: obj
      end type avovan_wrap_struct

      end module avovan_wrap_module

!!---------------------------- create ------------------------------------!!
!!---------------------------- create ------------------------------------!!
!!---------------------------- create ------------------------------------!!

      subroutine avovan_wrap_create (fpoint)  
      use avovan_wrap_module
      implicit none
      type(avovan_wrap_struct),intent(out)   :: fpoint
      type(avovan_struct)     ,pointer       :: obj           ! local

      nullify (obj)               ! needed for intel compiler.
      call avovan_create (obj)
      fpoint%obj => obj

      end subroutine avovan_wrap_create

!!---------------------------- delete ------------------------------------!!
!!---------------------------- delete ------------------------------------!!
!!---------------------------- delete ------------------------------------!!

      subroutine avovan_wrap_delete (fpoint)   
      use avovan_wrap_module
      implicit none
      type(avovan_wrap_struct),intent(inout) :: fpoint
      type(avovan_struct)     ,pointer       :: obj           ! local

      obj => fpoint%obj
      if (.not. associated(obj)) return
      call avovan_delete (obj)
      fpoint%obj => obj

      end subroutine avovan_wrap_delete

!!---------------------------- update ------------------------------------!!
!!---------------------------- update ------------------------------------!!
!!---------------------------- update ------------------------------------!!

      subroutine avovan_wrap_update (fpoint)     
      use avovan_wrap_module
      implicit none
      type(avovan_wrap_struct),intent(inout) :: fpoint
      type(avovan_struct)     ,pointer       :: obj           ! local

      obj => fpoint%obj
      if (.not. associated(obj)) return
      call avovan_update (obj)

      end subroutine avovan_wrap_update

!!---------------------------- wrapup ------------------------------------!!
!!---------------------------- wrapup ------------------------------------!!
!!---------------------------- wrapup ------------------------------------!!

      subroutine avovan_wrap_wrapup (fpoint)      
      use avovan_wrap_module
      implicit none
      type(avovan_wrap_struct),intent(inout) :: fpoint
      type(avovan_struct)     ,pointer       :: obj           ! local

      obj => fpoint%obj
      if (.not. associated(obj)) return
      call avovan_wrapup (obj)

      end subroutine avovan_wrap_wrapup

!!---------------------------- oneset ------------------------------------!!
!!---------------------------- oneset ------------------------------------!!
!!---------------------------- oneset ------------------------------------!!

      subroutine avovan_wrap_oneset (fpoint,ntr,  &
                                   hd,tr,lenhd,lentr,num)
      use avovan_wrap_module
      implicit none
      type(avovan_wrap_struct),intent(inout) :: fpoint
      integer               ,intent(in)    :: lenhd,lentr,num  ! argument
      integer               ,intent(inout) :: ntr              ! argument
      double precision      ,intent(inout) :: hd(lenhd,num)    ! argument
      real                  ,intent(inout) :: tr(lentr,num)    ! argument
      type(avovan_struct)     ,pointer       :: obj              ! local

      obj => fpoint%obj
      if (.not. associated(obj)) return
      call avovan (obj,ntr,hd,tr)                ! might be commented out.

      end subroutine avovan_wrap_oneset

!!---------------------------- twosets ------------------------------------!!
!!---------------------------- twosets ------------------------------------!!
!!---------------------------- twosets ------------------------------------!!

      subroutine avovan_wrap_twosets (fpoint,ntr,                 &
                                    hd1,tr1,lenhd1,lentr1,num1, &
                                    hd2,tr2,lenhd2,lentr2,num2)
      use avovan_wrap_module
      implicit none
      type(avovan_wrap_struct),intent(inout) :: fpoint
      integer               ,intent(in)    :: lenhd1,lentr1,num1 ! argument
      integer               ,intent(in)    :: lenhd2,lentr2,num2 ! argument
      integer               ,intent(inout) :: ntr                ! argument
      double precision      ,intent(inout) :: hd1(lenhd1,num1)   ! argument
      real                  ,intent(inout) :: tr1(lentr1,num1)   ! argument
      double precision      ,intent(inout) :: hd2(lenhd2,num2)   ! argument
      real                  ,intent(inout) :: tr2(lentr2,num2)   ! argument
      type(avovan_struct)     ,pointer       :: obj                ! local

      obj => fpoint%obj
      if (.not. associated(obj)) return
!!!!  call avovan (obj,ntr,hd1,tr1,hd2,tr2)      ! might be commented out.

      end subroutine avovan_wrap_twosets

!!----------------------------- end ---------------------------------------!!
!!----------------------------- end ---------------------------------------!!
!!----------------------------- end ---------------------------------------!!