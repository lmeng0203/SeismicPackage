!!------------------------ tsmute_wrap.f90 --------------------------!!
!!------------------------ tsmute_wrap.f90 --------------------------!!
!!------------------------ tsmute_wrap.f90 --------------------------!!

!!!!!!!!!! DO NOT EDIT THIS FILE - it is machine generated !!!!!!!!!!

!!------------------------------ module -----------------------------------!!
!!------------------------------ module -----------------------------------!!
!!------------------------------ module -----------------------------------!!

      module tsmute_wrap_module
      use tsmute_module
      implicit none
      public

      type :: tsmute_wrap_struct
        type(tsmute_struct),pointer :: obj
      end type tsmute_wrap_struct

      end module tsmute_wrap_module

!!---------------------------- create ------------------------------------!!
!!---------------------------- create ------------------------------------!!
!!---------------------------- create ------------------------------------!!

      subroutine tsmute_wrap_create (fpoint)  
      use tsmute_wrap_module
      implicit none
      type(tsmute_wrap_struct),intent(out)   :: fpoint
      type(tsmute_struct)     ,pointer       :: obj           ! local

      nullify (obj)               ! needed for intel compiler.
      call tsmute_create (obj)
      fpoint%obj => obj

      end subroutine tsmute_wrap_create

!!---------------------------- delete ------------------------------------!!
!!---------------------------- delete ------------------------------------!!
!!---------------------------- delete ------------------------------------!!

      subroutine tsmute_wrap_delete (fpoint)   
      use tsmute_wrap_module
      implicit none
      type(tsmute_wrap_struct),intent(inout) :: fpoint
      type(tsmute_struct)     ,pointer       :: obj           ! local

      obj => fpoint%obj
      if (.not. associated(obj)) return
      call tsmute_delete (obj)
      fpoint%obj => obj

      end subroutine tsmute_wrap_delete

!!---------------------------- update ------------------------------------!!
!!---------------------------- update ------------------------------------!!
!!---------------------------- update ------------------------------------!!

      subroutine tsmute_wrap_update (fpoint)     
      use tsmute_wrap_module
      implicit none
      type(tsmute_wrap_struct),intent(inout) :: fpoint
      type(tsmute_struct)     ,pointer       :: obj           ! local

      obj => fpoint%obj
      if (.not. associated(obj)) return
      call tsmute_update (obj)

      end subroutine tsmute_wrap_update

!!---------------------------- wrapup ------------------------------------!!
!!---------------------------- wrapup ------------------------------------!!
!!---------------------------- wrapup ------------------------------------!!

      subroutine tsmute_wrap_wrapup (fpoint)      
      use tsmute_wrap_module
      implicit none
      type(tsmute_wrap_struct),intent(inout) :: fpoint
      type(tsmute_struct)     ,pointer       :: obj           ! local

      obj => fpoint%obj
      if (.not. associated(obj)) return
      call tsmute_wrapup (obj)

      end subroutine tsmute_wrap_wrapup

!!---------------------------- oneset ------------------------------------!!
!!---------------------------- oneset ------------------------------------!!
!!---------------------------- oneset ------------------------------------!!

      subroutine tsmute_wrap_oneset (fpoint,ntr,  &
                                   hd,tr,lenhd,lentr,num)
      use tsmute_wrap_module
      implicit none
      type(tsmute_wrap_struct),intent(inout) :: fpoint
      integer               ,intent(in)    :: lenhd,lentr,num  ! argument
      integer               ,intent(inout) :: ntr              ! argument
      double precision      ,intent(inout) :: hd(lenhd,num)    ! argument
      real                  ,intent(inout) :: tr(lentr,num)    ! argument
      type(tsmute_struct)     ,pointer       :: obj              ! local

      obj => fpoint%obj
      if (.not. associated(obj)) return
      call tsmute (obj,ntr,hd,tr)                ! might be commented out.

      end subroutine tsmute_wrap_oneset

!!---------------------------- twosets ------------------------------------!!
!!---------------------------- twosets ------------------------------------!!
!!---------------------------- twosets ------------------------------------!!

      subroutine tsmute_wrap_twosets (fpoint,ntr,                 &
                                    hd1,tr1,lenhd1,lentr1,num1, &
                                    hd2,tr2,lenhd2,lentr2,num2)
      use tsmute_wrap_module
      implicit none
      type(tsmute_wrap_struct),intent(inout) :: fpoint
      integer               ,intent(in)    :: lenhd1,lentr1,num1 ! argument
      integer               ,intent(in)    :: lenhd2,lentr2,num2 ! argument
      integer               ,intent(inout) :: ntr                ! argument
      double precision      ,intent(inout) :: hd1(lenhd1,num1)   ! argument
      real                  ,intent(inout) :: tr1(lentr1,num1)   ! argument
      double precision      ,intent(inout) :: hd2(lenhd2,num2)   ! argument
      real                  ,intent(inout) :: tr2(lentr2,num2)   ! argument
      type(tsmute_struct)     ,pointer       :: obj                ! local

      obj => fpoint%obj
      if (.not. associated(obj)) return
!!!!  call tsmute (obj,ntr,hd1,tr1,hd2,tr2)      ! might be commented out.

      end subroutine tsmute_wrap_twosets

!!----------------------------- end ---------------------------------------!!
!!----------------------------- end ---------------------------------------!!
!!----------------------------- end ---------------------------------------!!